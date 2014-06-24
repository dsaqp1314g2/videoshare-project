var API_BASE_URL = "http://localhost:8080/videoshare-api/videoshare";
//window.onload = getVideos();
var videoid_now;
var cookies = document.cookie;
var username = getCookie("username");
var userpass = getCookie("userpass");

var loginlogout; 

$("#button_getvideos").click(function(e) {
	e.preventDefault();
	getVideos();
});

$("#button_login").click(function(e) {
	e.preventDefault();
   
	if(getCookie('username')=="") //no se ha hecho loggin:
	{
		var newLogin = new Object();
		newLogin.username = $("#username").val()
		newLogin.userpass= $("#password").val()
		loginlogout = "1";				 
		Login(newLogin);
	}
	else{
		 $('<div class="alert alert-danger"> <strong>¡Error!</strong> Antes debes cerrar sesión en la cuenta actual. </div>').appendTo($("#loginform"));
	}
		
});

$("#button_logout").click(function(e) {
	e.preventDefault();
	
	if(getCookie('username')=="") //ha hecho login
	{
		 $('<div class="alert alert-danger"> <strong>¡Error!</strong> Para poder cerrar sesión tienes que haber iniciado sesión... ¡Prueba a registrarte y después Log in! </div>').appendTo($("#loginform"));
	}
	else
	{
		Logout();
		window.location = "Videoshare.html";
	}
});
// * * * * *  Botones de categorias * * * * * * * 
$("#button_cienciaficcion").click(function(e) {
	e.preventDefault();
    var categoria = "ciencia_ficcion";     
	$("#videos_result").text(' ');
	buscarPorCategoria(categoria);
});

$("#button_comedia").click(function(e) {
	e.preventDefault();
    var categoria = "comedia";     
	$("#videos_result").text(' ');
	buscarPorCategoria(categoria);
});

$("#button_animales").click(function(e) {
	e.preventDefault();
    var categoria = "animales";     
	$("#videos_result").text(' ');
	buscarPorCategoria(categoria);
});

$("#button_infantil").click(function(e) {
	e.preventDefault();
    var categoria = "infantil";     
	$("#videos_result").text(' ');	
	buscarPorCategoria(categoria);
});

$("#button_informativo").click(function(e) {
	e.preventDefault();
    var categoria = "informativo";     
	$("#videos_result").text(' ');
	buscarPorCategoria(categoria);
});

$("#button_sexo").click(function(e) {
	e.preventDefault();
    var categoria = "porn";     
	$("#videos_result").text(' ');
	buscarPorCategoria(categoria);
});

$("#button_terror").click(function(e) {
	e.preventDefault();
    var categoria = "terror";     
	$("#videos_result").text(' ');	
	buscarPorCategoria(categoria);
});
// * * * * *  Hasta aquí Botones de categorias * * * * * * * 

// * * * * *  Registrarse/Login/shareit * * * * * * * 
$("#button_registrarse").click(function(e) {
    e.preventDefault();
                         
    var newSignin = new Object();
    newSignin.username = $("#username").val();
    newSignin.userpass = $("#password").val();
	newSignin.name = $("#name").val();
   // newSignin.password2 = $("#password2").val()
    newSignin.email = $("#mail").val();
   // newSignin.mail2 = $("#mail2").val()
    
    //comprobarPassword(newSignin.password, newSignin.password2);
    //comprobarMail(newSignin.mail, newSignin.mail2);
                               
    Signin(newSignin);
});
$("#button_shareit").click(function(e) {
    e.preventDefault();
  
    shareVideo();
});
$("#button_updateit").click(function(e) {
    e.preventDefault();
                         
    var newUpdateVideo = new Object();
    newUpdateVideo.username = $("#username").val()
    newUpdateVideo.nombre_video = $("#nombre_video").val()
    newUpdateVideo.categoria = $("#categoria_video").val()
    
  
    updateVideo(newUpdateVideo, videoid_now);
});
$("#button_video").click(function(e) {
    e.preventDefault();
                         
    var newVideo = new Object();
    newVideo.username = $("#username").val()
    newVideo.nombre_video = $("#nombre_video").val()
    newVideo.categoria = $("#categoria_video").val()
    
  
    Signin(newSignin);
});
$("#button_postreview").click(function(e) {
    e.preventDefault();
                         
    var newReview = new Object();
    newReview.username = getCookie('username');
	newReview.reviewtext= $("#textA").val();
	newReview.videoid=videoid_now;
	newReview.fecha = "17-7-2014";
  
    postReview(newReview, newReview.videoid);
	
});
// * * * * *  hasta aquí * * * * * * * 

// * * * * *  Botones de ordenar por --- * * * * * * * 
$("#button_puntuacion").click(function(e) {
	e.preventDefault();  
	$("#videos_result").text(' ');
	var ordenarpor = "bypuntuacion";
	 getVideosByX(ordenarpor);
});

$("#button_fecha").click(function(e) {
	e.preventDefault();  
	$("#videos_result").text(' ');
	 var ordenarpor = "byfecha";
	 getVideosByX(ordenarpor);
});

$("#button_categoria").click(function(e) {
	e.preventDefault();  
	$("#videos_result").text(' ');
	 var ordenarpor = "bycategoria";
	 getVideosByX(ordenarpor);
});

$("#button_username").click(function(e) {
	e.preventDefault();  
	$("#videos_result").text(' ');
	 var ordenarpor = "byusername";
	 getVideosByX(ordenarpor);
});

// * * * * * Hasta aquñi botones de ordenar por --- * * * * * * * 

// métodos:
function getVideos(){
    var url = "http://localhost:8080/videoshare-api/videoshare";
	$("#videos_result").text(' ');
	
	$.ajax({
           url : url,
           type : 'GET',
           dataType : 'json',
           crossDomain : true,
           
    }).done(function(data, status, jqxhr) {
        var videos = data;
        
            $.each(videos.video, function(i, v){
                   var video = v;
				    //$('#newReviewForm').appendTo($('#videos_result'));
					document.getElementById("newReviewForm").style.visibility="hidden";
				   $('<form class="form-horizontal" style="background-color:#F0EEEA" role="form">').appendTo($('#videos_result'));
				   $('<div class="form-group" style="background-color:#F0EEEA">').appendTo($('#videos_result'));
                   $('<h4><b> Título del Vídeo: ' + video.nombre_video+ '</b></h4>').appendTo($('#videos_result'));
                   $('<h5> Username: ' + video.username + '</h5>').appendTo($('#videos_result'));
                   $('<h5> Fecha: ' + video.fecha+ '</h5>').appendTo($('#videos_result'));
				   $('<h5> URL: ' + video.url+ '</h5>').appendTo($('#videos_result'));
				   
				    //añadimos para reproducir el vídeo en html el siguiente código:
				    $(' <div class="col-sm-12"><video id ="demo' + video.videoid +'" src="'+ video.url+'" type="video/webm" controls>Tu navegador no implementa el elemento <code>video</code>.<div><button onclick="document.getElementById(\'demo ' + video.videoid +'\').play()">Reproducir el Audio</button><button onclick="document.getElementById(\'demo ' + video.videoid +'\').pause()">Pausar el Audio</button><button onclick="document.getElementById(\'demo ' + video.videoid +'\').volume+=0.1">Aumentar el Volumen</butto<button onclick="document.getElementById(\'demo ' + video.videoid +'\').volume-=0.1">Disminuir el Volumen</button></div></video></div>').appendTo($('#videos_result'));
               
                   var categoria = video.categorias[0];

                   $('<strong> Categoria: </strong> ' + categoria.categoria + '<br>').appendTo($('#videos_result'));
                   $('<button type="button" class="btn success" id="button_video" style="color:white;background-color:#3E3E3E;width:90px; height:4" onClick="getVideoById('+video.videoid+')">Video ' + video.videoid + '</button>').appendTo($('#videos_result'));
				   
				   $('</div>').appendTo($('#videos_result'));
				   $('</form>').appendTo($('#videos_result'));
				   $('<div class ="col-sm-12" style="padding: 3px 10px 1px 10px;" ><center><legend></legend></center><br></div></div>').appendTo($('#videos_result'));
				   
            });
            
          
            
    }).fail(function() {
		$("#videos_result").text("No caargaaaaaaaa");
		$('<div class="alert alert-danger"> <strong>Oh!</strong> No se ha podido cargar la lista de Videos. </div>').appendTo($("#videos_result"));
    });
}
function getVideoById(videoid)
{
	var url = "http://localhost:8080/videoshare-api/videoshare/"+videoid;
	$("#videos_result").text(' ');
	videoid_now = videoid;
	$.ajax({
           url : url,
           type : 'GET',
           crossDomain : true,
           
    }).done(function(data, status, jqxhr) {
        var videos = data;
           
				
					$('<center><div class ="col-sm-12" style="padding: 10px 10px 11px 0px;" ><h3><b>' + videos.nombre_video+ '</b></h3></div></center>').appendTo($('#videos_result'));
							 //añadimos para reproducir el vídeo en html el siguiente código:
				    $(' <div class="col-sm-12"><video id ="demo" src="'+ videos.url+'" type="video/webm" controls>Tu navegador no implementa el elemento <code>video</code>.<div><button onclick="document.getElementById(\'demo\').play()">Reproducir el Audio</button><button onclick="document.getElementById(\'demo\').pause()">Pausar el Audio</button><button onclick="document.getElementById(\'demo\').volume+=0.1">Aumentar el Volumen</butto<button onclick="document.getElementById(\'demo\').volume-=0.1">Disminuir el Volumen</button></div></video></div>').appendTo($('#videos_result'));
               
			   
					$('<div class ="col-sm-12" style="padding: 10px 10px 15px 110px;" ><div class="btn-toolbar" role="toolbar"> <div class="btn-group"><div class=class ="col-sm-8"><div class="btn-group"><button type="button" style="color:white;background-color:#3E3E3E;width:110px; height:4" class="btn dropdown-toggle"data-toggle="dropdown"> Puntuación <span class="caret"></span></button><ul class="dropdown-menu"><li><a onClick="postPuntuacion('+videos.videoid+',1)">1</a></li><li><a onClick="postPuntuacion('+videos.videoid+',2)">2</a></li><li><a onClick="postPuntuacion('+videos.videoid+',3)">3</a></li><li><a onClick="postPuntuacion('+videos.videoid+',4)">4</a></li><li><a onClick="postPuntuacion('+videos.videoid+',5)">5</a></li><li><a onClick="postPuntuacion('+videos.videoid+',6)">6</a></li><li><a onClick="postPuntuacion('+videos.videoid+',7)">7</a></li><li><a onClick="postPuntuacion('+videos.videoid+',8)">8</a></li><li><a onClick="postPuntuacion('+videos.videoid+',9)">9</a></li><li><a onClick="postPuntuacion('+videos.videoid+',10)">10</a></li></ul></div><button type="button" class="btn success" id="button_updatevideo" style="color:white;background-color:#3E3E3E;width:90px; height:4" onClick="location.href=\'EditarVideo.html\'"> Editar </button><button type="button" class="btn success" id="button_postreview" style="color:white;background-color:#CB2626;width:90px; height:4"onClick="deleteVideo('+videos.videoid+')"> Eliminar </button></div></div></div></div>').appendTo($('#videos_result'));
					
					
					$('<h5><strong>Username: ' + videos.username + '</strong></h5>').appendTo($('#videos_result'));
					$('<h5> <strong>Fecha: ' + videos.fecha+ '</strong></h5>').appendTo($('#videos_result'));
					   
				
                   var categoria = videos.categorias[0];
                   $('<strong> Categoria: </strong> ' + categoria.categoria + '<br>').appendTo($('#videos_result'));
                   
				   
                   var puntuaciones = videos.puntuaciones[0];
				   if(puntuaciones==null)
				   {
						$('<strong> Puntuacion: </strong> no hay puntuación registrada. <strong>¡Se el primero!</strong> <br>').appendTo($('#videos_result'));
				   }
				   else
				   {
						$('<strong> Puntuacion: </strong> ' + puntuaciones.puntuacion + '<br></div>').appendTo($('#videos_result'));
					}
					
					$('<center><legend> Comentarios </legend></center><br></div>').appendTo($('#videosreviews_result'));
				   var reviews = videos.reviews[0];
				   if (reviews == null)
				   {
						$('<strong><h5> Comentarios: </strong></h5> No hay comentarios. <strong>¡Sé el primero!</strong> <br>').appendTo($('#videosreviews_result'));
				   }
				   else
				   {
					document.getElementById("newReviewForm").style.visibility="visible";
				    $.each(videos.reviews, function(i, r)
						{
							var rev=r;
						   $('<div class ="col-sm-12" style="padding: 3px 10px 1px 10px;" ><h5> <strong> ' + rev.username + '</strong></h5><h5> '+ rev.reviewtext + '</h5><legend></legend></div>').appendTo($('#videosreviews_result'));
					   
					   });
				   }	
				   	  
    }).fail(function() {
		$("#videos_result").text("No caargaaaaaaaa");
		$('<div class="alert alert-danger"> <strong>Oh!</strong> No se ha podido cargar la lista de Videos. </div>').appendTo($("#videos_result"));
    });

}

function getVideosByX(ordenarpor)
{
    var url = "http://localhost:8080/videoshare-api/videoshare/" + ordenarpor;
	$("#videos_result").text(' ');
	
	$.ajax({
           url : url,
           type : 'GET',
           dataType : 'json',
           crossDomain : true,
           
    }).done(function(data, status, jqxhr) {
        var videos = data;
        
            $.each(videos.video, function(i, v){
                   var video = v;
				   $('<form class="form-horizontal" style="background-color:#F0EEEA" role="form">').appendTo($('#videos_result'));
				   $('<div class="form-group" style="background-color:#F0EEEA">').appendTo($('#videos_result'));
                   $('<h4><b> Título del Vídeo: ' + video.nombre_video+ '</b></h4>').appendTo($('#videos_result'));
                   $('<h5> Username: ' + video.username + '</h5>').appendTo($('#videos_result'));
                   $('<h5> Fecha: ' + video.fecha+ '</h5>').appendTo($('#videos_result'));
				   $('<h5> URL: ' + video.url+ '</h5>').appendTo($('#videos_result'));
				   
				    //añadimos para reproducir el vídeo en html el siguiente código:
				    $(' <div class="col-sm-12"><video id ="demo' + video.videoid +'" src="'+ video.url+'" type="video/webm" controls>Tu navegador no implementa el elemento <code>video</code>.<div><button onclick="document.getElementById(\'demo ' + video.videoid +'\').play()">Reproducir el Audio</button><button onclick="document.getElementById(\'demo ' + video.videoid +'\').pause()">Pausar el Audio</button><button onclick="document.getElementById(\'demo ' + video.videoid +'\').volume+=0.1">Aumentar el Volumen</butto<button onclick="document.getElementById(\'demo ' + video.videoid +'\').volume-=0.1">Disminuir el Volumen</button></div></video></div>').appendTo($('#videos_result'));
               
                   var categoria = video.categorias[0];

                   $('<strong> Categoria: </strong> ' + categoria.categoria + '<br>').appendTo($('#videos_result'));
                   $('<button type="button" class="btn success" id="button_video" style="color:white;background-color:#3E3E3E;width:90px; height:4" onClick="getVideoById('+video.videoid+')">Video ' + video.videoid + '</button>').appendTo($('#videos_result'));
				   
				   $('</div>').appendTo($('#videos_result'));
				   $('</form>').appendTo($('#videos_result'));
				   
				    $('<div class ="col-sm-12" style="padding: 3px 10px 1px 10px;" ><center><legend></legend></center><br></div></div>').appendTo($('#videos_result'));
				   
            });
            
          
            
    }).fail(function() {
		$("#videos_result").text("No caargaaaaaaaa");
		$('<div class="alert alert-danger"> <strong>Oh!</strong> No se ha podido cargar la lista de Videos. </div>').appendTo($("#videos_result"));
    });
}

function buscarPorCategoria(categoria)
{
    var url = "http://localhost:8080/videoshare-api/videoshare/searchc?categoria="+categoria;
	$("#videos_result").text(' ');
	
	$.ajax({
           url : url,
           type : 'GET',
           crossDomain : true,
           
    }).done(function(data, status, jqxhr) {
        var videos = data;
            $.each(videos, function(i, v){
                   var video = v[0];
				   //console.log(video[0]);
                   $('<h4><b> Título del Vídeo: ' + video.nombre_video+ '</b></h4>').appendTo($('#videos_result'));
                   $('<h5> Username: ' + video.username + '</h5>').appendTo($('#videos_result'));
                   $('<h5> Fecha: ' + video.fecha+ '</h5>').appendTo($('#videos_result'));
                    //añadimos para reproducir el vídeo en html el siguiente código:
				    $(' <div class="col-sm-12"><video id ="demo" src="'+ video.url+'" type="video/webm" controls>Tu navegador no implementa el elemento <code>video</code>.<div><button onclick="document.getElementById(\'demo\').play()">Reproducir el Audio</button><button onclick="document.getElementById(\'demo\').pause()">Pausar el Audio</button><button onclick="document.getElementById(\'demo\').volume+=0.1">Aumentar el Volumen</butto<button onclick="document.getElementById(\'demo\').volume-=0.1">Disminuir el Volumen</button></div></video></div>').appendTo($('#videos_result'));
               
                   var categoria = video.categorias[0];
                   $('<strong> Categoria: </strong> ' + categoria.categoria + '<br>').appendTo($('#videos_result'));
                   
                   var puntuacion = video.puntuaciones[0];
                   $('<strong> Puntuacion: </strong> ' + puntuacion.puntuacion + '<br>').appendTo($('#videos_result'));
                   $('<button type="button" class="btn success" id="button_video" style="color:white;background-color:#3E3E3E;width:90px; height:4" onClick="getVideoById('+video.videoid+')">Video ' + video.videoid + '</button>').appendTo($('#videos_result'));
				   $('<div class ="col-sm-12" style="padding: 3px 10px 1px 10px;" ><center><legend></legend></center><br></div></div>').appendTo($('#videos_result'));
				   
            });
                        
            
    }).fail(function() {
		$("#videos_result").text("No caargaaaaaaaa");
		$('<div class="alert alert-danger"> <strong>Oh!</strong> No se ha podido cargar la lista de Videos. </div>').appendTo($("#videos_result"));
    });
}

function Login(newLogin) {
    var username = newLogin.username;
    var password = newLogin.password;
	var data = JSON.stringify(newLogin);
	var url = API_BASE_URL +"/login";
	$("#videos_result").text('');
	
		//mandamos a la API la user+password y comprobamos que sea correcto
		$.ajax({
			url : url,
			type : 'POST',
			crossDomain : true,
			dataType : 'json',
			data : data,
			cache : false,
			contentType : "application/vnd.videoshare.api.user+json; charset=UTF-8 ",
			processData : false
           
           }).done(function(data, status, jqxhr) {
                $('<div class="alert alert-success"> <strong>Wellcome to Videoshare! </strong></div>').appendTo($("#loginform"));
			
				setCookie('username', username, 1 );
				var usernamec = getCookie('username');
				login = "1"; 	
				window.location = "Videoshare.html";
            }).fail(function() {
                    $('<div class="alert alert-danger"> <strong>Oh!</strong> Error </div>').appendTo($("#loginform"));
            });

}
function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    var expires = "expires="+d.toGMTString();
    document.cookie = cname + "=" + cvalue + "; " + expires;
}
function getCookie(cname) {
	var name = cname + "=";
	var ca = document.cookie.split(';');
	for (var i = 0; i < ca.length; i++) {
		var c = ca[i].trim();
		if (c.indexOf(name) == 0)
			return c.substring(name.length, c.length);
	}
	return "";
}
/*
function comprobarPassword(pass1, pass2)
{
    if (pass1 == pass2)
    {
        $('<div class="alert alert-success"> <strong>Ok!</strong> Las constraseñas son iguales </div>').appendTo($("#login_result"));;;
    }
    else
    {
        $('<div class="alert alert-danger"> <strong>Oh!</strong>Las contraseñas no son iguales</div>').appendTo($("#signin_result"));
    }

}

function comprobarMail(mail1, mail2)
{
    if (mail1 == mail2)
    {
        $('<div class="alert alert-success"> <strong>OK!</strong>Las direcciones de correo son iguales. </div>').appendTo($("#signin_result"));;;
    }
    else
    {
        $('<div class="alert alert-danger"> <strong>Oh!</strong>Las direcciones de correo no son iguales. </div>').appendTo($("#signin_result"));
    }
}*/
function Logout()
{
	//eliminamos la cookie
	document.cookie = 'username=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
	loginlogout= "0"; 	
	
}

function deleteVideo(videoid)
{
	var url = API_BASE_URL+"/"+videoid; 
	$("#videos_result").text(' ');
    


		//mandamos a la API la user+password y comprobamos que sea correcto
		$.ajax({
			url : url,
           type : 'DELETE',
           dataType : 'json',
           crossDomain : true,
           
           }).done(function(data, status, jqxhr) {
					$("#videos_result").text(' ');
					//window.onload = getVideos();
                   $('<div class="alert alert-success"> <strong>Wellcome to Videoshare! </strong> You were signed in.</div>').appendTo($("#videos_result"));
            }).fail(function() {
                    $('<div class="alert alert-danger"> <strong>Oh!</strong> Error </div>').appendTo($("#videos_result"));
            });

}

function Signin(newSignin) {
	var url = API_BASE_URL+"/users"; //falta añadir la URL para comprobar el usuario
	$("#videos_result").text(' ');
    var data = JSON.stringify(newSignin);


		//mandamos a la API la user+password y comprobamos que sea correcto
		$.ajax({
			url : url,
			type : 'POST',
			crossDomain : true,
			dataType : 'json',
			data : data,
			cache : false,
			contentType : "application/vnd.videoshare.api.user+json; charset=UTF-8 ",
			processData : false
           
           }).done(function(data, status, jqxhr) {
                   $('<div class="alert alert-success"> <strong>Wellcome to Videoshare! </strong> You were signed in.</div>').appendTo($("#loginform"));
				   window.location = "Login.html";
            }).fail(function() {
                    $('<div class="alert alert-danger"> <strong>Oh!</strong> Error </div>').appendTo($("#loginform"));
            });

	  }

function postReview(newReview,videoid) {
	
	var url = API_BASE_URL +"/"+videoid+ "/reviews"; //falta añadir la URL para comprobar el usuario
	$("#videos_result").text(' ');
	
	var data = JSON.stringify(newReview);
		//mandamos a la API la user+password y comprobamos que sea correcto
		$.ajax({
			url : url,
			type : 'POST',
			crossDomain : true,
			dataType : 'json',
			data : data,
			cache : false,
			contentType : "application/vnd.videoshare.api.reviews+json; charset=UTF-8 ",
			processData : false
           
           }).done(function(data, status, jqxhr) {
                   //$('<div class="alert alert-success"> <strong> Comentario añadido </strong></div>').appendTo($("#videos_result"));
				   $("#videosreviews_result").text(' ');
				   getVideoById(videoid);
				   
            }).fail(function() {
                    $('<div class="alert alert-danger"> <strong>Oh!</strong> Error </div>').appendTo($("#videos_result"));
            });

	  }
	  
function postPuntuacion(videoid, puntuacion) {
var url = API_BASE_URL +"/"+videoid+ "/puntuacion"; //falta añadir la URL para comprobar el usuario
	$("#videos_result").text(' ');
   
	var newPuntuacion = new Object();
	newPuntuacion.username = getCookie('username');
	newPuntuacion.puntuacion= puntuacion; 
	newPuntuacion.videoid=videoid;

	var data = JSON.stringify(newPuntuacion);
		//mandamos a la API la user+password y comprobamos que sea correcto
		$.ajax({
            url : url,
			type : 'POST',
			crossDomain : true,
			dataType : 'json',
			data : data,
			cache : false,
			contentType : "application/vnd.videoshare.api.puntuacion+json; charset=UTF-8 ",
			processData : false
           
           }).done(function(data, status, jqxhr) {
                   $('<div class="alert alert-success"> <strong>Puntuación añadida</strong>.</div>').appendTo($("#videos_result"));
				   getVideoById(videoid)
            }).fail(function() {
                    $('<div class="alert alert-danger"> <strong>Oh!</strong> Error </div>').appendTo($("#videos_result"));
            });
}
	  
function shareVideo() {
	var URL = API_BASE_URL ;
	$("#videos_result").text(' ');
    //var data = JSON.stringify(newVideo);
	var formData = new FormData($('form')[0]);
	/*
	$.ajax({
           url : url,
           type : 'POST',
           crossDomain : true,
           dataType : 'json',
           data: data,
           
           }).done(function(data, status, jqxhr) {
                   $('<div class="alert alert-success"> <strong> El video se ha subido correctamente </strong></div>').appendTo($("#shareit_result"));
            }).fail(function() {
                    $('<div class="alert alert-danger"> <strong>Oh!</strong> Error </div>').appendTo($("#shareit_result"));
            });*/
			
			
	$.ajax({
	url : URL+ '/upload',
	type : 'POST',
	xhr : function() {
		var myXhr = $.ajaxSettings.xhr();
		if (myXhr.upload) {
			myXhr.upload.addEventListener('progress',
					progressHandlingFunction, false); 									// upload
			}
			return myXhr;
		},
		crossDomain : true,
		data : formData,
		cache : false,
		contentType : false,
		processData : false
	}).done(function(data, status, jqxhr) {
			var response = $.parseJSON(jqxhr.responseText);
			lastFilename = response.filename;
			$('#sharevideo').attr('src', response.videoURL);
			$('progress').toggle();

			
	}).fail(function(jqXHR, textStatus) {
				alert("KO");
				console.log(textStatus);
	});
    

}

function progressHandlingFunction(e) {
	if (e.lengthComputable) {
		$('progress').attr({
			value : e.loaded,
			max : e.total
		});
	}
}

function updateVideo(newVideo, videoid) {
	
	var url = API_BASE_URL + "/"+ videoid;
	$("#videos_result").text(' ');
    var data = JSON.stringify(newVideo);
	
	$.ajax({
           url : url,
           type : 'PUT',
           crossDomain : true,
           dataType : 'json',
           data: data,
           
           }).done(function(data, status, jqxhr) {
                   $('<div class="alert alert-success"> <strong> El video se ha subido correctamente </strong></div>').appendTo($("#videos_result"));
            }).fail(function() {
                    $('<div class="alert alert-danger"> <strong>Oh!</strong> Error </div>').appendTo($("#videos_result"));
            });
    

}



//Cargar código html en videos_result

function cargarNewReview()
{
	$("#videos_result").text(' ');
	html="";
	
	html +='<div class="col-sm-8">';
    html +=' <div class="panel">';
	html +='<div class="panel-body">';
						
	html +='<div class="form-group">';
	html +='<legend>New Review</legend>';
	html +='<label for="inputTitle">Añade comentario</label>';
	html +='<center><strong> <form role="form"><textarea class="form-control" id="textA" rows="3"></textarea></form></center>';
	html +='</div>';
    html +='</div>';
						
	html +='<div class="panel-body">';
	html +='<div style="float:right" >';
	html +='<button type="button" class="btn success" id="button_cancelar" style="color:white;background-color:black;width:90px; height:4;" onClick="location.href=\'Videoshare.html\'"> Cancelar </button>';
	html +='</div>';
	html +='<div style="float:right" >';
	html +='<button type="button" class="btn success" id="button_comentario" style="color:white;background-color:black;width:140px; height:4;"> Enviar Comentario </button>';
	html +='</div>'
	html +='</div>'
    html +='<div class="panel-body">';
    html +='<div class ="col-sm-8">';
    html +='<span id="video_result"></span>';
    html +='</div>';
	html +='</div>';
	html +='</div>';
    html +='</div><!-- /.col-sm-8 -->';
       
	$("#videos_result").html(html);
}
