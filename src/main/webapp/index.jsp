<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Home - Peer Skill Exchange" />
<jsp:include page="/WEB-INF/partials/header.jsp" />
<jsp:include page="/WEB-INF/partials/navbar.jsp" />

<main>
    <!-- Hero Section -->
    <section class="hero-section text-center text-lg-start">
        <div class="container">
            <div class="row align-items-center g-5">
                <div class="col-lg-7">
                    <div class="d-inline-flex align-items-center gap-2 px-3 py-1 mb-3 rounded-pill bg-primary-subtle text-primary border border-primary-subtle small fw-bold">
                        <i class="bi bi-mortarboard-fill"></i> Exclusively for SRM UNIVERSITY AP Students
                    </div>
                    <h1 class="hero-headline">
                        Learn Together. Teach Together. <br><span class="gradient-text">Grow Together.</span>
                    </h1>
                    <p class="hero-subtitle">
                        Share what you know, discover what you love, and connect with peers who make learning a two-way experience at SRM University AP.
                    </p>
                    <div class="d-flex flex-wrap gap-3 justify-content-center justify-content-lg-start">
                        <a href="${pageContext.request.contextPath}/register" class="btn btn-primary btn-lg px-4">
                            <i class="bi bi-person-plus me-1"></i> Get Started (5 Free Credits)
                        </a>
                        <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-primary btn-lg px-4">
                            <i class="bi bi-box-arrow-in-right me-1"></i> Student Sign In
                        </a>
                    </div>
                    
                    <div class="d-flex align-items-center gap-4 mt-5 pt-3 border-top border-secondary-subtle">
                        <div>
                            <div class="fw-bold fs-4 text-dark">15+</div>
                            <div class="small text-muted">Skill Domains</div>
                        </div>
                        <div class="border-start ps-4">
                            <div class="fw-bold fs-4 text-dark">100%</div>
                            <div class="small text-muted">Peer-to-Peer Free</div>
                        </div>
                        <div class="border-start ps-4">
                            <div class="fw-bold fs-4 text-dark">5 Credits</div>
                            <div class="small text-muted">Welcome Bonus</div>
                        </div>
                    </div>
                </div>

                <div class="col-lg-5">
                    <div class="card-custom p-4 shadow-lg border-0 bg-white position-relative">
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <span class="badge badge-custom badge-mutual">
                                <i class="bi bi-stars"></i> Mutual Skill Match
                            </span>
                            <span class="match-score-pill">95% Match</span>
                        </div>
                        
                        <div class="d-flex align-items-center gap-3 mb-3">
                            <div class="avatar avatar-lg bg-primary text-white">AS</div>
                            <div>
                                <h5 class="fw-bold mb-0 text-dark">Aarav Sharma</h5>
                                <div class="small text-muted">CSE &bull; 3rd Year &bull; SRM AP</div>
                                <div class="star-rating small mt-1">
                                    <i class="bi bi-star-fill text-warning"></i>
                                    <i class="bi bi-star-fill text-warning"></i>
                                    <i class="bi bi-star-fill text-warning"></i>
                                    <i class="bi bi-star-fill text-warning"></i>
                                    <i class="bi bi-star-fill text-warning"></i>
                                    <span class="text-dark fw-bold ms-1">5.0</span>
                                </div>
                            </div>
                        </div>

                        <div class="p-3 bg-light rounded-3 mb-3">
                            <div class="small fw-bold text-uppercase text-muted mb-1">Teaches (What you want)</div>
                            <span class="badge badge-custom badge-offer me-1">Java Programming</span>
                            <span class="badge badge-custom badge-offer">Web Development</span>
                        </div>

                        <div class="p-3 bg-light rounded-3 mb-3">
                            <div class="small fw-bold text-uppercase text-muted mb-1">Wants to Learn (What you teach)</div>
                            <span class="badge badge-custom badge-want">Graphic Designing</span>
                        </div>

                        <div class="d-grid">
                            <a href="${pageContext.request.contextPath}/login" class="btn btn-primary">
                                <i class="bi bi-arrow-left-right me-1"></i> Send Exchange Request
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- How It Works Section -->
    <section id="how-it-works" class="py-5 bg-white border-top border-bottom">
        <div class="container py-4">
            <div class="text-center max-w-700 mx-auto mb-5">
                <span class="text-primary fw-bold text-uppercase small tracking-wide">Structured Knowledge Sharing</span>
                <h2 class="fw-bold text-dark mt-2">How SkillSwap Works</h2>
                <p class="text-muted">A fair, collaborative model empowering every SRM University AP student.</p>
            </div>

            <div class="row g-4">
                <div class="col-md-3">
                    <div class="stat-card h-100 text-center">
                        <div class="stat-icon indigo mx-auto"><i class="bi bi-person-vcard"></i></div>
                        <h5 class="fw-bold">1. Create Profile</h5>
                        <p class="small text-muted mb-0">Register with your SRM AP ID (AP***********), add skills you can teach, and topics you want to learn.</p>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="stat-card h-100 text-center">
                        <div class="stat-icon purple mx-auto"><i class="bi bi-search-heart"></i></div>
                        <h5 class="fw-bold">2. Smart Matching</h5>
                        <p class="small text-muted mb-0">Our multi-factor matching engine finds students with complementary skills and overlapping availability.</p>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="stat-card h-100 text-center">
                        <div class="stat-icon emerald mx-auto"><i class="bi bi-calendar-event"></i></div>
                        <h5 class="fw-bold">3. Schedule Session</h5>
                        <p class="small text-muted mb-0">Send an exchange proposal, agree on timings, and meet online (Google Meet) or at campus spots like Central Library.</p>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="stat-card h-100 text-center">
                        <div class="stat-icon amber mx-auto"><i class="bi bi-coin"></i></div>
                        <h5 class="fw-bold">4. Earn & Review</h5>
                        <p class="small text-muted mb-0">Confirm completion together to unlock 2 credits per session, and leave ratings to build peer reputation.</p>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- Popular Skills -->
    <section id="popular-skills" class="py-5">
        <div class="container py-4">
            <div class="d-flex justify-content-between align-items-end mb-4">
                <div>
                    <span class="text-primary fw-bold text-uppercase small">High Demand Domains</span>
                    <h2 class="fw-bold text-dark mt-1">Trending Skills at SRM AP</h2>
                </div>
                <a href="${pageContext.request.contextPath}/skills" class="btn btn-outline-primary d-none d-md-inline-flex align-items-center gap-1">
                    View All Skills <i class="bi bi-arrow-right"></i>
                </a>
            </div>

            <div class="row g-4">
                <div class="col-lg-4 col-md-6">
                    <div class="card-custom p-4 h-100">
                        <div class="d-flex align-items-center gap-3 mb-3">
                            <div class="stat-icon indigo mb-0"><i class="bi bi-code-slash"></i></div>
                            <div>
                                <h5 class="fw-bold mb-0">Java Programming</h5>
                                <span class="badge bg-light text-secondary border">Programming</span>
                            </div>
                        </div>
                        <p class="small text-muted">Core Java, OOP concepts, Collections, Exception Handling, Multithreading, and JDBC for backend systems.</p>
                        <div class="mt-auto pt-3 border-top d-flex justify-content-between align-items-center">
                            <span class="small text-muted"><i class="bi bi-people me-1"></i> Popular in CSE</span>
                            <a href="${pageContext.request.contextPath}/login" class="text-primary fw-bold small text-decoration-none">Explore &rarr;</a>
                        </div>
                    </div>
                </div>

                <div class="col-lg-4 col-md-6">
                    <div class="card-custom p-4 h-100">
                        <div class="d-flex align-items-center gap-3 mb-3">
                            <div class="stat-icon purple mb-0"><i class="bi bi-palette"></i></div>
                            <div>
                                <h5 class="fw-bold mb-0">Graphic & UI Design</h5>
                                <span class="badge bg-light text-secondary border">Design</span>
                            </div>
                        </div>
                        <p class="small text-muted">Figma, Adobe Photoshop, design tokens, color psychology, and wireframing for student hackathons.</p>
                        <div class="mt-auto pt-3 border-top d-flex justify-content-between align-items-center">
                            <span class="small text-muted"><i class="bi bi-people me-1"></i> SRM AP Media Club</span>
                            <a href="${pageContext.request.contextPath}/login" class="text-primary fw-bold small text-decoration-none">Explore &rarr;</a>
                        </div>
                    </div>
                </div>

                <div class="col-lg-4 col-md-6">
                    <div class="card-custom p-4 h-100">
                        <div class="d-flex align-items-center gap-3 mb-3">
                            <div class="stat-icon emerald mb-0"><i class="bi bi-cpu"></i></div>
                            <div>
                                <h5 class="fw-bold mb-0">Machine Learning</h5>
                                <span class="badge bg-light text-secondary border">AI & DS</span>
                            </div>
                        </div>
                        <p class="small text-muted">Supervised and unsupervised models, computer vision, NumPy, and PyTorch fundamentals.</p>
                        <div class="mt-auto pt-3 border-top d-flex justify-content-between align-items-center">
                            <span class="small text-muted"><i class="bi bi-people me-1"></i> AI & DS Department</span>
                            <a href="${pageContext.request.contextPath}/login" class="text-primary fw-bold small text-decoration-none">Explore &rarr;</a>
                        </div>
                    </div>
                </div>

                <div class="col-lg-4 col-md-6">
                    <div class="card-custom p-4 h-100">
                        <div class="d-flex align-items-center gap-3 mb-3">
                            <div class="stat-icon amber mb-0"><i class="bi bi-mic"></i></div>
                            <div>
                                <h5 class="fw-bold mb-0">Public Speaking</h5>
                                <span class="badge bg-light text-secondary border">Communication</span>
                            </div>
                        </div>
                        <p class="small text-muted">Speech framing, overcoming stage fright, voice modulation, and elevator pitches for interviews.</p>
                        <div class="mt-auto pt-3 border-top d-flex justify-content-between align-items-center">
                            <span class="small text-muted"><i class="bi bi-people me-1"></i> Toastmasters SRM AP</span>
                            <a href="${pageContext.request.contextPath}/login" class="text-primary fw-bold small text-decoration-none">Explore &rarr;</a>
                        </div>
                    </div>
                </div>

                <div class="col-lg-4 col-md-6">
                    <div class="card-custom p-4 h-100">
                        <div class="d-flex align-items-center gap-3 mb-3">
                            <div class="stat-icon indigo mb-0"><i class="bi bi-camera"></i></div>
                            <div>
                                <h5 class="fw-bold mb-0">Photography & Lightroom</h5>
                                <span class="badge bg-light text-secondary border">Creative Arts</span>
                            </div>
                        </div>
                        <p class="small text-muted">Manual camera settings, aperture, shutter speed, composition rules, and color grading in Lightroom.</p>
                        <div class="mt-auto pt-3 border-top d-flex justify-content-between align-items-center">
                            <span class="small text-muted"><i class="bi bi-people me-1"></i> Photography Club</span>
                            <a href="${pageContext.request.contextPath}/login" class="text-primary fw-bold small text-decoration-none">Explore &rarr;</a>
                        </div>
                    </div>
                </div>

                <div class="col-lg-4 col-md-6">
                    <div class="card-custom p-4 h-100">
                        <div class="d-flex align-items-center gap-3 mb-3">
                            <div class="stat-icon emerald mb-0"><i class="bi bi-wallet2"></i></div>
                            <div>
                                <h5 class="fw-bold mb-0">Financial Literacy</h5>
                                <span class="badge bg-light text-secondary border">Life Skills</span>
                            </div>
                        </div>
                        <p class="small text-muted">Budgeting pocket money, understanding mutual funds, credit cards, and developing smart money habits in college.</p>
                        <div class="mt-auto pt-3 border-top d-flex justify-content-between align-items-center">
                            <span class="small text-muted"><i class="bi bi-people me-1"></i> SEAMS Business</span>
                            <a href="${pageContext.request.contextPath}/login" class="text-primary fw-bold small text-decoration-none">Explore &rarr;</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- Benefits / Community Banner -->
    <section id="benefits" class="py-5 bg-white border-top">
        <div class="container py-4">
            <div class="row align-items-center g-5">
                <div class="col-lg-6">
                    <span class="text-primary fw-bold text-uppercase small">Why Peer Exchange?</span>
                    <h2 class="fw-bold text-dark mt-2 mb-3">Break Free from Expensive Courses and One-Way Tutorials</h2>
                    <p class="text-muted mb-4">
                        Conventional platforms offer pre-recorded videos where you can't ask immediate doubts. SkillSwap connects you with actual college peers who understand your curriculum, campus schedule, and practical problems.
                    </p>
                    <div class="d-flex flex-column gap-3">
                        <div class="d-flex align-items-start gap-3">
                            <i class="bi bi-check-circle-fill text-success fs-5 mt-1"></i>
                            <div>
                                <h6 class="fw-bold mb-1">Mutual Knowledge Trading</h6>
                                <p class="small text-muted mb-0">Learn Java from someone while teaching them Graphic Design. Zero monetary cost.</p>
                            </div>
                        </div>
                        <div class="d-flex align-items-start gap-3">
                            <i class="bi bi-check-circle-fill text-success fs-5 mt-1"></i>
                            <div>
                                <h6 class="fw-bold mb-1">Campus-Coordinated Learning</h6>
                                <p class="small text-muted mb-0">Coordinate in-person sessions at SRM AP ALC rooms, Central Library, or online via Google Meet.</p>
                            </div>
                        </div>
                        <div class="d-flex align-items-start gap-3">
                            <i class="bi bi-check-circle-fill text-success fs-5 mt-1"></i>
                            <div>
                                <h6 class="fw-bold mb-1">Verified SRM AP Student Community</h6>
                                <p class="small text-muted mb-0">Exclusive to verified SRM University AP student IDs and campus credentials.</p>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="col-lg-6">
                    <div class="wallet-card">
                        <div class="d-flex justify-content-between align-items-start mb-4">
                            <div>
                                <div class="text-uppercase small tracking-wide opacity-75">Virtual Learning Credits</div>
                                <div class="wallet-balance mt-1">5 Credits</div>
                            </div>
                            <span class="badge bg-white text-dark fw-bold px-3 py-2 rounded-pill">Welcome Bonus</span>
                        </div>
                        <p class="small opacity-75 mb-4">
                            Every newly registered student receives 5 platform credits. Earn 2 credits every time you complete teaching a peer.
                        </p>
                        <div class="d-flex gap-3">
                            <a href="${pageContext.request.contextPath}/register" class="btn btn-light text-dark fw-bold px-4">
                                Claim 5 Welcome Credits
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</main>

<jsp:include page="/WEB-INF/partials/footer.jsp" />

