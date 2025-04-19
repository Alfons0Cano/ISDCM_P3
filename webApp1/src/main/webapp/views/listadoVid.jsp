<%-- 
    Document   : listadoVid
    Created on : Feb 27, 2025
    Author     : isdcm
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>${misVideos ? 'Mis Videos' : 'Explorar'} - VideoWeb</title>
        <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/navbar.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/listadoVid.css">
    </head>
    <body class="dashboard">
        <jsp:include page="/partials/navbar.jsp" />
        
        <div class="container-fluid px-4">
            <!-- Filter section -->
            <div class="filter-section">
                <form id="searchForm" class="row g-3" onsubmit="return false;">
                    <div class="col-md-5 col-lg-6">
                        <div class="input-group">
                            <input type="text" class="form-control" id="titulo" name="titulo" value="${param.titulo}" placeholder="Buscar por título...">
                            <button type="submit" class="btn btn-primary">
                                <i class="bi bi-search"></i>
                            </button>
                        </div>
                    </div>
                    <div class="col-md-3 col-lg-3">
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-person"></i></span>
                            <input type="text" class="form-control" id="autor" name="autor" value="${param.autor}" placeholder="Autor">
                        </div>
                    </div>
                    <div class="col-md-3 col-lg-3">
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-calendar"></i></span>
                            <input type="date" class="form-control" id="fecha" name="fecha" value="${param.fecha}">
                        </div>
                    </div>
                </form>
            </div>

            <!-- Results section -->
            <div id="resultados" class="video-grid">
                <c:choose>
                    <c:when test="${empty videos}">
                        <div class="alert" role="alert">
                            <i class="bi bi-camera-reels" style="font-size: 2rem; display: block; margin-bottom: 1rem;"></i>
                            <h4>No hay videos disponibles</h4>
                            <p class="mb-0">¿Por qué no empiezas <a href="${pageContext.request.contextPath}/videos/registro" class="alert-link">subiendo un video</a>?</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${videos}" var="video">
                            <a href="${pageContext.request.contextPath}/videos/play/${video.id}" class="video-card-link">
                                <div class="video-card">
                                    <div class="video-thumbnail">
                                        <div class="thumbnail-placeholder">
                                            <i class="bi bi-play-circle-fill"></i>
                                        </div>
                                        <span class="video-duration">${video.duracion}</span>
                                    </div>
                                    <div class="video-info">
                                        <div class="channel-avatar">
                                            ${fn:substring(video.autor, 0, 1).toUpperCase()}
                                        </div>
                                        <div class="video-info-text">
                                            <h3 class="video-title">${video.titulo}</h3>
                                            <div class="video-details">
                                                <span class="video-author">${video.autor}</span>
                                                <div class="video-stats">
                                                    <span class="video-views">${video.reproducciones} visualizaciones</span>
                                                    <span class="video-date">${video.fechaCreacion}</span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </a>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            // Add event listeners to filter chips
            document.querySelectorAll('.filter-chip').forEach(chip => {
                chip.addEventListener('click', function() {
                    // Remove active class from all chips
                    document.querySelectorAll('.filter-chip').forEach(c => {
                        c.classList.remove('active');
                    });
                    // Add active class to clicked chip
                    this.classList.add('active');
                    // Aquí se podría implementar el filtrado real
                });
            });
        </script>

        <script>
            // Wait for the DOM to be fully loaded
            document.addEventListener('DOMContentLoaded', function() {
                const searchForm = document.getElementById('searchForm');
                if (searchForm) {
                    searchForm.addEventListener('submit', function(e) {
                        e.preventDefault();
                        
                        // Recoger valores del formulario
                        const titulo = document.getElementById('titulo').value;
                        const autor = document.getElementById('autor').value;
                        const fecha = document.getElementById('fecha').value;
                        
                        // Construir la URL con parámetros de consulta
                        let url = '${pageContext.request.contextPath}/rest/search?';
                        const params = [];
                        
                        if (titulo.trim()) params.push('titulo=' + encodeURIComponent(titulo));
                        if (autor.trim()) params.push('autor=' + encodeURIComponent(autor));
                        if (fecha.trim()) params.push('fecha=' + encodeURIComponent(fecha));
                        
                        url += params.join('&');
                        
                        // Realizar la petición fetch
                        fetch(url)
                            .then(response => {
                                if (!response.ok) {
                                    throw new Error('Network response was not ok');
                                }
                                return response.json();
                            })
                            .then(data => {
                                console.log('Datos recibidos:', data); // Para depuración
                                const resultados = document.getElementById('resultados');
                                resultados.innerHTML = ''; // Limpiar resultados anteriores
                                
                                // Verificar si hay un error en la respuesta
                                if (data.error) {
                                    resultados.innerHTML = 
                                        '<div class="alert alert-danger" role="alert">' +
                                        '<i class="bi bi-exclamation-triangle" style="font-size: 2rem; display: block; margin-bottom: 1rem;"></i>' +
                                        '<h4>Error al buscar videos</h4>' +
                                        '<p class="mb-0">' + data.error + '</p>' +
                                        '</div>';
                                    return;
                                }
                                
                                if (!Array.isArray(data) || data.length === 0) {
                                    resultados.innerHTML = 
                                        '<div class="alert" role="alert">' +
                                        '<i class="bi bi-camera-reels" style="font-size: 2rem; display: block; margin-bottom: 1rem;"></i>' +
                                        '<h4>No se encontraron videos</h4>' +
                                        '<p class="mb-0">No hay videos que coincidan con tu búsqueda.</p>' +
                                        '</div>';
                                    return;
                                }
                                
                                // Construir lista de resultados
                                data.forEach(video => {
                                    const videoCard = document.createElement('a');
                                    videoCard.href = '${pageContext.request.contextPath}/videos/play/' + video.id;
                                    videoCard.className = 'video-card-link';
                                    
                                    // Formatear la duración
                                    let duracionStr = '';
                                    if (video.duracion) {
                                        const partes = video.duracion.split(":");
                                        if (partes.length === 3) {
                                            const horas = parseInt(partes[0], 10);
                                            const minutos = parseInt(partes[1], 10);
                                            const segundos = parseInt(partes[2], 10);
                                            
                                            if (horas > 0) {
                                                duracionStr = horas + 'h ' + minutos + 'm ' + segundos + 's';
                                            } else {
                                                duracionStr = minutos + 'm ' + segundos + 's';
                                            }
                                        } else {
                                            duracionStr = video.duracion;
                                        }
                                    }
                                    
                                    // Preparar valores seguros para mostrar
                                    const autorDisplay = video.autor ? video.autor : '';
                                    const autorInitial = autorDisplay ? autorDisplay.charAt(0).toUpperCase() : '';
                                    const tituloDisplay = video.titulo ? video.titulo : '';
                                    const reproduccionesDisplay = typeof video.reproducciones === 'number' ? video.reproducciones : 0;
                                    const fechaDisplay = video.fechaCreacion ? video.fechaCreacion : '';
                                    
                                    // Crear el contenido del video card usando concatenación en lugar de template literals
                                    const videoCardContent = 
                                        '<div class="video-card">' +
                                            '<div class="video-thumbnail">' +
                                                '<div class="thumbnail-placeholder">' +
                                                    '<i class="bi bi-play-circle-fill"></i>' +
                                                '</div>' +
                                                '<span class="video-duration">' + duracionStr + '</span>' +
                                            '</div>' +
                                            '<div class="video-info">' +
                                                '<div class="channel-avatar">' +
                                                    autorInitial +
                                                '</div>' +
                                                '<div class="video-info-text">' +
                                                    '<h3 class="video-title">' + tituloDisplay + '</h3>' +
                                                    '<div class="video-details">' +
                                                        '<span class="video-author">' + autorDisplay + '</span>' +
                                                        '<div class="video-stats">' +
                                                            '<span class="video-views">' + reproduccionesDisplay + ' visualizaciones</span>' +
                                                            '<span class="video-date">' + fechaDisplay + '</span>' +
                                                        '</div>' +
                                                    '</div>' +
                                                '</div>' +
                                            '</div>' +
                                        '</div>';
                                    
                                    videoCard.innerHTML = videoCardContent;
                                    resultados.appendChild(videoCard);
                                });
                            })
                            .catch(error => {
                                console.error('Error al buscar videos:', error);
                                const resultados = document.getElementById('resultados');
                                resultados.innerHTML = 
                                    '<div class="alert alert-danger" role="alert">' +
                                    '<i class="bi bi-exclamation-triangle" style="font-size: 2rem; display: block; margin-bottom: 1rem;"></i>' +
                                    '<h4>Error al buscar videos</h4>' +
                                    '<p class="mb-0">Ha ocurrido un error al realizar la búsqueda. Por favor, inténtalo de nuevo.</p>' +
                                    '</div>';
                            });
                    });
                }
            });
        </script>

    </body>
</html>