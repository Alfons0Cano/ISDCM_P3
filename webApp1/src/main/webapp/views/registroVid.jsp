<%-- 
    Document   : registroVid
    Created on : Feb 27, 2025
    Author     : isdcm
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Registro de Video - VideoWeb</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/registroVid.css">
    </head>
    <body class="form-page">
        <jsp:include page="/partials/navbar.jsp" />
        
        <div class="container mt-4">
            <!-- Page header -->
            <div class="page-header mb-4 d-flex align-items-center justify-content-between">
                <div class="d-flex align-items-center">
                    <i class="bi bi-film me-3" style="font-size: 2rem; color: #3a7bd5;"></i>
                    <h3 class="mb-0">Registro de Video</h3>
                </div>
                <div>
                    <a href="${pageContext.request.contextPath}/videos/lista" class="btn btn-outline-primary">
                        <i class="bi bi-arrow-left me-1"></i> Volver al listado
                    </a>
                </div>
            </div>
            
            <div class="row justify-content-center">
                <div class="col-lg-10 col-md-11">
                    <div class="card shadow form-card">
                        <div class="card-header text-center">
                            <h3>Nuevo Video</h3>
                            <p class="text-muted small mb-0 mt-1">Complete el formulario para subir un nuevo video</p>
                        </div>
                        <div class="card-body">
                            <% if(request.getAttribute("error") != null) { %>
                                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                    <i class="bi bi-exclamation-triangle-fill me-2"></i>
                                    <%= request.getAttribute("error") %>
                                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                                </div>
                            <% } %>
                            
                            <% if(request.getAttribute("success") != null) { %>
                                <div class="alert alert-success alert-dismissible fade show" role="alert">
                                    <i class="bi bi-check-circle-fill me-2"></i>
                                    <%= request.getAttribute("success") %>
                                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                                </div>
                            <% } %>
                            
                            <form action="${pageContext.request.contextPath}/videos/registro" method="post" enctype="multipart/form-data" id="videoForm">
                                <div class="form-section">
                                    <h5 class="border-start border-primary border-4 ps-2"><i class="bi bi-info-circle me-2"></i>Información Básica</h5>
                                    
                                    <div class="mb-3">
                                        <label for="titulo" class="form-label fw-semibold">Título <span class="text-danger">*</span></label>
                                        <div class="input-group">
                                            <span class="input-icon"><i class="bi bi-type"></i></span>
                                            <input type="text" class="form-control" id="titulo" name="titulo" required 
                                                   placeholder="Ingrese el título del video">
                                        </div>
                                        <div class="form-text">El título debe ser único entre tus videos</div>
                                    </div>
                                    
                                    <div class="mb-3">
                                        <label for="descripcion" class="form-label fw-semibold">Descripción</label>
                                        <div class="position-relative">
                                            <span class="input-icon-static"><i class="bi bi-card-text"></i></span>
                                            <textarea class="form-control" id="descripcion" name="descripcion" rows="3" 
                                                      style="padding-left: 3rem;" placeholder="Ingrese una descripción (opcional)"></textarea>
                                        </div>
                                    </div>
                                </div>
                                
                                <div class="form-section">
                                    <h5 class="border-start border-primary border-4 ps-2"><i class="bi bi-upload me-2"></i>Archivo de Video <span class="text-danger">*</span></h5>
                                    
                                    <div class="mb-4">
                                        <div class="file-upload-container">
                                            <div class="file-upload-area" id="drop-area">
                                                <input type="file" class="form-control" id="videoFile" name="videoFile" 
                                                    accept="video/mp4,video/avi,video/mkv,video/mov,video/wmv" required
                                                    style="display: none;">
                                                
                                                <div class="text-center p-4 upload-prompt">
                                                    <i class="bi bi-cloud-arrow-up display-4 text-primary mb-3"></i>
                                                    <h5>Arrastra y suelta tu video aquí</h5>
                                                    <p class="text-muted">O</p>
                                                    <button type="button" class="btn btn-outline-primary" id="browseButton">
                                                        <i class="bi bi-folder2-open me-2"></i>Seleccionar archivo
                                                    </button>
                                                    <p class="mt-2 small text-muted">Formatos soportados: MP4, AVI, MKV, MOV, WMV</p>
                                                </div>
                                                
                                                <div class="file-info-container" style="display: none;">
                                                    <div class="card bg-light">
                                                        <div class="card-body">
                                                            <div class="d-flex align-items-center">
                                                                <i class="bi bi-file-earmark-play me-3 text-primary" style="font-size: 2rem;"></i>
                                                                <div class="flex-grow-1">
                                                                    <h6 class="mb-1 filename-text"></h6>
                                                                    <div class="small text-muted filesize-text"></div>
                                                                </div>
                                                                <button type="button" class="btn btn-sm btn-outline-danger remove-file">
                                                                    <i class="bi bi-x-lg"></i>
                                                                </button>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    
                                    <div class="row mb-3">
                                        <div class="col-12">
                                            <div class="progress" style="display: none;" id="uploadProgress">
                                                <div class="progress-bar progress-bar-striped progress-bar-animated" role="progressbar" style="width: 0%"></div>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="bg-light p-3 rounded mb-3">
                                        <h6 class="mb-2"><i class="bi bi-info-circle me-2"></i>Información automática</h6>
                                        <p class="small text-muted mb-0">
                                            El sistema extraerá automáticamente del video los siguientes metadatos:
                                            <span class="badge bg-secondary me-1">Fecha de creación</span>
                                            <span class="badge bg-secondary me-1">Duración</span>
                                            <span class="badge bg-secondary">Formato</span>
                                        </p>
                                    </div>
                                    <!-- El resto de los campos serán completados automáticamente -->
                                    <input type="hidden" id="automaticFecha" name="automaticFecha" value="true">
                                    <input type="hidden" id="videoDuration" name="videoDuration" value="">
                                    <input type="hidden" id="videoFormat" name="videoFormat" value="">
                                </div>
                                
                                <div class="d-grid gap-2 mt-4">
                                    <button type="submit" class="btn btn-primary btn-lg btn-submit" id="submitButton" disabled>
                                        <i class="bi bi-cloud-upload me-2"></i>Registrar Video
                                    </button>
                                </div>
                                
                                <div class="text-center mt-3">
                                    <span class="text-muted small">Los campos marcados con <span class="text-danger">*</span> son obligatorios</span>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            document.addEventListener('DOMContentLoaded', function() {
                const dropArea = document.getElementById('drop-area');
                const fileInput = document.getElementById('videoFile');
                const browseButton = document.getElementById('browseButton');
                const uploadPrompt = document.querySelector('.upload-prompt');
                const fileInfoContainer = document.querySelector('.file-info-container');
                const filenameText = document.querySelector('.filename-text');
                const filesizeText = document.querySelector('.filesize-text');
                const removeFileButton = document.querySelector('.remove-file');
                const submitButton = document.getElementById('submitButton');
                const titleInput = document.getElementById('titulo');
                const form = document.getElementById('videoForm');
                
                // Function to format file size
                function formatFileSize(bytes) {
                    if (bytes < 1024) return bytes + ' bytes';
                    else if (bytes < 1048576) return (bytes / 1024).toFixed(2) + ' KB';
                    else if (bytes < 1073741824) return (bytes / 1048576).toFixed(2) + ' MB';
                    else return (bytes / 1073741824).toFixed(2) + ' GB';
                }
                
                // Update the submit button state
                function updateSubmitButtonState() {
                    const isValid = fileInput.files.length > 0 && titleInput.value.trim() !== '';
                    submitButton.disabled = !isValid;
                    
                    if (isValid) {
                        submitButton.classList.remove('btn-secondary');
                        submitButton.classList.add('btn-primary');
                    } else {
                        submitButton.classList.remove('btn-primary');
                        submitButton.classList.add('btn-secondary');
                    }
                }
                
                // Handle file selection
                function handleFileSelect(file) {
                    if (!file.type.startsWith('video/')) {
                        showAlert('Por favor selecciona un archivo de video válido.', 'warning');
                        return;
                    }
                    
                    // Create URL for the video file
                    const videoURL = URL.createObjectURL(file);
                    
                    // Create video element to load metadata
                    const video = document.createElement('video');
                    video.preload = 'metadata';
                    
                    // Set up event handlers
                    video.onloadedmetadata = function() {
                        const durationSeconds = Math.round(video.duration);
                        document.getElementById('videoDuration').value = durationSeconds;
                        
                        // Update UI
                        uploadPrompt.style.display = 'none';
                        fileInfoContainer.style.display = 'block';
                        filenameText.textContent = file.name;
                        filesizeText.textContent = formatFileSize(file.size);
                        updateSubmitButtonState();
                        
                        // Clean up
                        URL.revokeObjectURL(videoURL);
                    };
                    
                    video.onerror = function() {
                        URL.revokeObjectURL(videoURL);
                        showAlert('Error al leer el video. Por favor, intente con otro archivo.');
                    };
                    
                    // Start loading the video
                    video.src = videoURL;
                }
                
                function showAlert(message, type = 'danger') {
                    const alertDiv = document.createElement('div');
                    alertDiv.className = 'alert alert-' + type + ' alert-dismissible fade show';
                    alertDiv.innerHTML = '<i class="bi ' + (type == 'danger' ? 'bi-exclamation-triangle-fill' : 'bi-info-circle-fill') + ' me-2"></i>' + 
                        message +
                        '<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>';
                    
                    // Insertar antes del formulario
                    const formElement = document.getElementById('videoForm');
                    formElement.parentNode.insertBefore(alertDiv, formElement);
                    
                    // Auto-dismiss after 5 seconds
                    setTimeout(() => {
                        alertDiv.classList.remove('show');
                        setTimeout(() => alertDiv.remove(), 150);
                    }, 5000);
                }
                
                // Click browse button to select file
                browseButton.addEventListener('click', function() {
                    fileInput.click();
                });
                
                // Handle file input change
                fileInput.addEventListener('change', function(e) {
                    if (this.files.length > 0) {
                        handleFileSelect(this.files[0]);
                    }
                });
                
                // Handle drag and drop
                ['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => {
                    dropArea.addEventListener(eventName, function(e) {
                        e.preventDefault();
                        e.stopPropagation();
                    }, false);
                });
                
                ['dragenter', 'dragover'].forEach(eventName => {
                    dropArea.addEventListener(eventName, function() {
                        dropArea.classList.add('highlight');
                    }, false);
                });
                
                ['dragleave', 'drop'].forEach(eventName => {
                    dropArea.addEventListener(eventName, function() {
                        dropArea.classList.remove('highlight');
                    }, false);
                });
                
                // Handle drop
                dropArea.addEventListener('drop', function(e) {
                    const file = e.dataTransfer.files[0];
                    fileInput.files = e.dataTransfer.files;
                    handleFileSelect(file);
                }, false);
                
                // Remove file
                removeFileButton.addEventListener('click', function() {
                    fileInput.value = '';
                    uploadPrompt.style.display = 'block';
                    fileInfoContainer.style.display = 'none';
                    updateSubmitButtonState();
                });
                
                // Update submit button on title change
                titleInput.addEventListener('input', updateSubmitButtonState);
                
                // Handle form submission
                form.addEventListener('submit', function(e) {
                    if (fileInput.files.length === 0) {
                        e.preventDefault();
                        showAlert('Por favor selecciona un archivo de video.');
                        return;
                    }
                    
                    if (titleInput.value.trim() === '') {
                        e.preventDefault();
                        showAlert('Por favor ingresa un título para el video.');
                        return;
                    }
                    
                    document.getElementById('uploadProgress').style.display = 'block';
                    submitButton.disabled = true;
                    submitButton.innerHTML = '<i class="bi bi-hourglass-split me-2"></i>Subiendo video...';
                    
                    // Here you would normally implement XHR upload with progress
                    // For simplicity, we'll just simulate progress
                    const progressBar = document.querySelector('.progress-bar');
                    let progress = 0;
                    
                    const interval = setInterval(function() {
                        progress += 5;
                        progressBar.style.width = progress + '%';
                        progressBar.setAttribute('aria-valuenow', progress);
                        
                        if (progress >= 100) {
                            clearInterval(interval);
                        }
                    }, 100);
                });
            });
        </script>
        
        <style>
            .file-upload-area {
                border: 2px dashed #adb5bd;
                border-radius: 10px;
                transition: all 0.3s ease;
                position: relative;
                min-height: 200px;
                background-color: #f9fafc;
            }
            
            .file-upload-area.highlight {
                border-color: #3a7bd5;
                background-color: rgba(58, 123, 213, 0.1);
            }
            
            .file-upload-area .upload-prompt {
                padding: 40px 20px;
            }
            
            .file-info-container {
                padding: 10px;
            }
            
            #drop-area {
                cursor: pointer;
            }
            
            .progress {
                height: 10px;
                margin-top: 15px;
                border-radius: 5px;
                overflow: hidden;
            }
            
            .form-section {
                position: relative;
                margin-bottom: 2rem;
                padding-bottom: 1.5rem;
                border-bottom: 1px solid rgba(0,0,0,0.05);
            }
            
            .form-section:last-child {
                border-bottom: none;
            }
            
            .card.shadow {
                box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.1) !important;
            }
            
            .form-control:focus {
                box-shadow: 0 0 0 0.25rem rgba(58, 123, 213, 0.25);
            }
            
            .btn-submit {
                transition: all 0.3s ease;
            }
        </style>
    </body>
</html>