<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="newMain"/>
		<title>Blog <g:message code="top.header.brand"/></title>
	</head>
	<body>
        <section class="pageheader-default text-center">
            <div class="semitransparentbg">
                <h1 class="animated fadeInLeftBig notransition">Blog Home</h1>
            </div>
        </section>
        <div class="wrapsemibox">
          <section class="container animated fadeInDown notransition" id="blog_div">
            <div class="row blogindex">
                <!-- MAIN -->
                <div class="col-md-9">
                    <div class="text-center">
                        <h2>Merry Christmas</h2>
                        <span class="meta bottomspace30">By <a href="#" title="Posts by Roy Sanders" rel="author">Roy Sanders</a><span class="bullet">•</span> November 5, 2013 <span class="bullet">•</span><a href="#" title="View all posts in Food &amp; Health" rel="category tag">Food &amp; Health</a></span>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <p class="lead">
                                <img src="${assetPath(src: 'open/student.png')}" class="pull-left img-responsive" alt="" style="max-width:180px;">
                                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque rutrum pellentesque imperdiet. Nulla lacinia iaculis nulla non pulvinar. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Ut eu risus enim, ut pulvinar lectus.
                            </p>
                            <a href="${g.createLink(controller: 'blog',action: 'story',id: 1)}" class="btn btn-default">Read more</a>
                            <hr class="clearfix">
                            <div class="text-center">
                                <h2>Finding Treasures</h2>
                                <span class="meta bottomspace30">By <a href="#" title="Posts by Roy Sanders" rel="author">Roy Sanders</a> <span class="bullet">•</span> November 5, 2013 <span class="bullet">•</span> <a href="#" title="View all posts in Food &amp; Health" rel="category tag">Food &amp; Health</a></span>
                            </div>
                            <p class="lead">
                                <img src="${assetPath(src: 'open/student1.png')}" class="pull-left img-responsive" alt="" style="max-width:180px;">
                                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque rutrum pellentesque imperdiet. Nulla lacinia iaculis nulla non pulvinar. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Ut eu risus enim, ut pulvinar lectus.
                            </p>
                            <a href="${g.createLink(controller: 'blog',action: 'story',id: 2)}" class="btn btn-default">Read more</a>
                            <hr class="clearfix">
                            <div class="text-center">
                                <h2>Once Upon a Time</h2>
                                <span class="meta bottomspace30">By <a href="#" title="Posts by Roy Sanders" rel="author">Roy Sanders</a><span class="bullet">•</span> November 5, 2013 <span class="bullet">•</span><a href="#" title="View all posts in Food &amp; Health" rel="category tag">Food &amp; Health</a></span>
                            </div>
                            <p class="lead">
                                <img src="${assetPath(src: 'open/student2.png')}" class="pull-left img-responsive" alt="" style="max-width:180px;">
                                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque rutrum pellentesque imperdiet. Nulla lacinia iaculis nulla non pulvinar. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Ut eu risus enim, ut pulvinar lectus.
                            </p>
                            <a href="${g.createLink(controller: 'blog',action: 'story',id: 3)}" class="btn btn-default">Read more</a>
                            <hr class="clearfix">
                            <ul class="pagination pagination-lg">
                                <li class="disabled"><a href="#">«</a></li>
                                <li class="active"><a href="#">1 <span class="sr-only">(current)</span></a></li>
                                <li><a href="#">2</a></li>
                                <li><a href="#">3</a></li>
                                <li><a href="#">4</a></li>
                                <li><a href="#">5</a></li>
                                <li><a href="#">»</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
                <!-- SIDEBAR -->
                <div class="col-md-3">
                    <aside class="sidebar topspace30">
                        <div class="wowwidget">
                            <h4>Categories</h4>
                            <ul class="categories">
                                <li><a href="#">Music & Films</a></li>
                                <li><a href="#">Home Decoration</a></li>
                                <li><a href="#">Food & Health</a></li>
                                <li><a href="#">Lifestyle Green</a></li>
                                <li><a href="#">Future Technology</a></li>
                            </ul>
                        </div>
                        <div class="wowwidget">
                            <div class="tabs">
                                <ul class="nav nav-tabs">
                                    <li class="active"><a href="#popularPosts" data-toggle="tab"><i class="icon icon-star"></i> Popular</a></li>
                                    <li><a href="#recentPosts" data-toggle="tab">Recent</a></li>
                                </ul>
                                <div class="tab-content">
                                    <div class="tab-pane active" id="popularPosts">
                                        <ul class="unstyled">
                                            <li>
                                                <div class="tabbedwidget">
                                                    <a href="blog-single.html">
                                                        <img src="${assetPath(src: 'open/student1.png')}" alt="">
                                                    </a>
                                                </div>
                                                <div class="post-info">
                                                    <a href="blog-single.html">Finding Treasures</a>
                                                    <div class="post-meta">
                                                        Dec 12, 2013
                                                    </div>
                                                </div>
                                            </li>
                                            <li>
                                                <div class="tabbedwidget">
                                                    <a href="blog-single.html">
                                                        <img src="${assetPath(src: 'open/student.png')}" alt="">
                                                    </a>
                                                </div>
                                                <div class="post-info">
                                                    <a href="blog-single.html">Once Upon a Time</a>
                                                    <div class="post-meta">
                                                        Jan 16, 2014
                                                    </div>
                                                </div>
                                            </li>
                                            <li>
                                                <div class="tabbedwidget">
                                                    <a href="blog-single.html">
                                                        <img src="${assetPath(src: 'open/student2.png')}" alt="">
                                                    </a>
                                                </div>
                                                <div class="post-info">
                                                    <a href="blog-single.html">Midnight in Paris</a>
                                                    <div class="post-meta">
                                                        Sep 28, 2014
                                                    </div>
                                                </div>
                                            </li>
                                        </ul>
                                    </div>
                                    <div class="tab-pane" id="recentPosts">
                                        <ul class="unstyled">
                                            <li>
                                                <div class="tabbedwidget">
                                                    <a href="blog-single.html">
                                                        <img src="${assetPath(src: 'open/student1.png')}" alt="">
                                                    </a>
                                                </div>
                                                <div class="post-info">
                                                    <a href="blog-single.html">From Rome with Love</a>
                                                    <div class="post-meta">
                                                        Jan 10, 2014
                                                    </div>
                                                </div>
                                            </li>
                                            <li>
                                                <div class="tabbedwidget">
                                                    <a href="blog-single.html">
                                                        <img src="${assetPath(src: 'open/student2.png')}" alt="">
                                                    </a>
                                                </div>
                                                <div class="post-info">
                                                    <a href="blog-single.html">Midnight in Paris</a>
                                                    <div class="post-meta">
                                                        Feb 13, 2014
                                                    </div>
                                                </div>
                                            </li>
                                            <li>
                                                <div class="tabbedwidget">
                                                    <a href="blog-single.html">
                                                        <img src="${assetPath(src: 'open/student.png')}" alt="">
                                                    </a>
                                                </div>
                                                <div class="post-info">
                                                    <a href="blog-single.html">City Lights</a>
                                                    <div class="post-meta">
                                                        Aug 25, 2014
                                                    </div>
                                                </div>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="widget">
                            <h4>About Us</h4>
                            <p>
                                Nulla nunc dui, tristique in semper vel, congue sed ligula. Nam dolor ligula, faucibus id sodales in, auctor fringilla libero. Nulla nunc dui, tristique in semper vel. Nam dolor ligula, faucibus id sodales in, auctor fringilla libero.
                            </p>
                        </div>
                    </aside>
                </div><!-- end sidebar -->
            </div>
          </section>
         </div>
	</body>
</html>
