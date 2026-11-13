<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<footer class="footer-custom">
    <div class="container">
        <div class="row g-4">
            <div class="col-lg-4 col-md-6">
                <div class="d-flex align-items-center gap-2 mb-3">
                    <span class="brand-badge bg-primary text-white p-2 rounded"><i class="bi bi-arrow-left-right"></i></span>
                    <h5 class="text-white mb-0 fw-bold">SkillSwap</h5>
                    <span class="badge bg-primary-subtle text-primary border border-primary-subtle">SRM AP</span>
                </div>
                <p class="text-secondary small">
                    A Student-to-Student Skill Exchange and Collaborative Learning Platform designed exclusively for SRM UNIVERSITY AP. Learn together. Teach together. Grow together.
                </p>
                <div class="d-flex gap-3 text-secondary mt-3">
                    <span class="badge bg-dark-subtle text-light border border-secondary border-opacity-25 py-2 px-3">
                        <i class="bi bi-geo-alt me-1 text-primary"></i> SRM University AP Campus, Neerukonda
                    </span>
                </div>
            </div>
            
            <div class="col-lg-2 col-md-6 col-6">
                <h6 class="text-white fw-bold mb-3">Platform</h6>
                <ul class="list-unstyled small d-flex flex-column gap-2 mb-0">
                    <li><a href="${pageContext.request.contextPath}/skills">Explore Skills</a></li>
                    <li><a href="${pageContext.request.contextPath}/matches">Smart Matches</a></li>
                    <li><a href="${pageContext.request.contextPath}/requests">Exchange Requests</a></li>
                    <li><a href="${pageContext.request.contextPath}/sessions">Learning Sessions</a></li>
                    <li><a href="${pageContext.request.contextPath}/credits">Credit Wallet</a></li>
                </ul>
            </div>

            <div class="col-lg-3 col-md-6 col-6">
                <h6 class="text-white fw-bold mb-3">SRM AP Domains</h6>
                <ul class="list-unstyled small d-flex flex-column gap-2 mb-0">
                    <li><span class="text-secondary">Computer Science & Eng.</span></li>
                    <li><span class="text-secondary">Electronics & Comm. Eng.</span></li>
                    <li><span class="text-secondary">Artificial Intelligence & DS</span></li>
                    <li><span class="text-secondary">Toastmasters & Orators Club</span></li>
                    <li><span class="text-secondary">SRM AP Media & Design Club</span></li>
                </ul>
            </div>

            <div class="col-lg-3 col-md-6">
                <h6 class="text-white fw-bold mb-3">Virtual Credit Economy</h6>
                <p class="small text-secondary mb-2">
                    5 Welcome Credits upon registration. Earn 2 credits for teaching, spend 2 credits for learning. Atomic transactions guaranteed.
                </p>
                <div class="p-2 rounded bg-dark border border-secondary border-opacity-25 small text-light">
                    <i class="bi bi-shield-check text-success me-1"></i> SRM AP Exclusive &bull; Non-monetary
                </div>
            </div>
        </div>

        <div class="footer-bottom d-flex flex-column flex-md-row justify-content-between align-items-center gap-2">
            <div class="text-secondary small">
                &copy; <%= java.time.Year.now().getValue() %> SkillSwap &bull; Advanced Java Collaborative Learning Platform &bull; SRM University AP.
            </div>
            <div class="text-secondary small">
                Built with Java Servlets, JSP, JDBC, MVC & MySQL
            </div>
        </div>
    </div>
</footer>

<!-- Bootstrap 5 Bundle JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<!-- SkillSwap Application JS -->
<script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
</body>
</html>

