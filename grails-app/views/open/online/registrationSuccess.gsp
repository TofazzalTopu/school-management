<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="open-ltpl"/>
    <title>Online Application - Step 4</title>
</head>

<body bgcolor="black">
<div class="wrapbox">

    <section class="pageheader-default text-center">
        <div class="semitransparentbg">
            <h1 class="animated fadeInLeftBig notransition">Online Application</h1>
        </div>
    </section>
   <div class="container">
  <h2>Application Preview</h2>
  <form class="form-horizontal" role="form" action="">

  <div class="form-group">
      <label class="control-label col-sm-2">Class Name:</label>
      <div class="col-sm-10">
          ${registration?.className?.name}
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-2" for="pwd">Name:</label>
      <div class="col-sm-10">
          ${registration?.name}
      </div>
    </div>
  </form>
</div>
</div>


</body>
</html>