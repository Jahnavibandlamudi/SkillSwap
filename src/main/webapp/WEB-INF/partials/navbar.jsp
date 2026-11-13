<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<nav class="navbar navbar-expand-lg navbar-custom sticky-top">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/${sessionScope.user != null ? (sessionScope.user.admin ? 'admin' : 'dashboard') : ''}">
            <span class="brand-badge"><i class="bi bi-arrow-left-right"></i></span>
            <span>SkillSwap</span>
            <span class="srm-tag">SRM AP</span>
        </a>
        
        <button class="navbar-toggler border-0 shadow-none" type="button" data-bs-toggle="collapse" data-bs-target="#navbarMain">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarMain">
            <ul class="navbar-nav ms-auto align-items-lg-center gap-1">
                <c:choose>
                    <c:when test="${sessionScope.user != null}">
                        <c:choose>
                            <c:when test="${sessionScope.user.admin}">
                                <li class="nav-item">
                                    <a class="nav-link ${activePage == 'admin' ? 'active' : ''}" href="${pageContext.request.contextPath}/admin">
                                        <i class="bi bi-speedometer2 me-1"></i> Admin Dashboard
                                    </a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link ${activePage == 'skills' ? 'active' : ''}" href="${pageContext.request.contextPath}/skills">
                                        <i class="bi bi-grid me-1"></i> Skills Catalog
                                    </a>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li class="nav-item">
                                    <a class="nav-link ${activePage == 'dashboard' ? 'active' : ''}" href="${pageContext.request.contextPath}/dashboard">
                                        <i class="bi bi-grid-1x2 me-1"></i> Dashboard
                                    </a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link ${activePage == 'skills' ? 'active' : ''}" href="${pageContext.request.contextPath}/skills">
                                        <i class="bi bi-compass me-1"></i> Explore Skills
                                    </a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link ${activePage == 'matches' ? 'active' : ''}" href="${pageContext.request.contextPath}/matches">
                                        <i class="bi bi-stars text-primary me-1"></i> Smart Matches
                                    </a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link ${activePage == 'requests' ? 'active' : ''}" href="${pageContext.request.contextPath}/requests">
                                        <i class="bi bi-arrow-repeat me-1"></i> Requests
                                    </a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link ${activePage == 'sessions' ? 'active' : ''}" href="${pageContext.request.contextPath}/sessions">
                                        <i class="bi bi-calendar3 me-1"></i> Sessions
                                    </a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link ${activePage == 'credits' ? 'active' : ''}" href="${pageContext.request.contextPath}/credits">
                                        <span class="badge bg-primary-subtle text-primary border border-primary-subtle px-2 py-1">
                                            <i class="bi bi-coin me-1"></i> ${sessionScope.user.creditBalance} Credits
                                        </span>
                                    </a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link position-relative ${activePage == 'notifications' ? 'active' : ''}" href="${pageContext.request.contextPath}/notifications" title="Notifications">
                                        <i class="bi bi-bell fs-5"></i>
                                        <c:if test="${unreadNotificationsCount > 0}">
                                            <span class="position-absolute top-1 start-100 translate-middle p-1 bg-danger border border-light rounded-circle">
                                                <span class="visually-hidden">New alerts</span>
                                            </span>
                                        </c:if>
                                    </a>
                                </li>
                            </c:otherwise>
                        </c:choose>

                        <!-- User Profile Dropdown -->
                        <li class="nav-item dropdown ms-lg-2">
                            <a class="nav-link dropdown-toggle d-flex align-items-center gap-2 p-1" href="#" data-bs-toggle="dropdown">
                                <span class="avatar avatar-sm bg-primary-subtle text-primary border" style="width: 36px; height: 36px; font-size: 0.9rem;">
                                    ${sessionScope.user.fullName != null ? sessionScope.user.fullName.substring(0, 1) : 'U'}
                                </span>
                                <span class="fw-semibold d-none d-xl-inline text-dark">${sessionScope.user.fullName}</span>
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end shadow border-0 mt-2 p-2" style="border-radius: var(--radius-md); min-width: 220px;">
                                <li class="px-3 py-2 border-bottom mb-1">
                                    <div class="fw-bold text-dark">${sessionScope.user.fullName}</div>
                                    <div class="small text-muted">${sessionScope.user.studentId} &bull; ${sessionScope.user.department}</div>
                                </li>
                                <c:if test="${!sessionScope.user.admin}">
                                    <li>
                                        <a class="dropdown-item rounded py-2" href="${pageContext.request.contextPath}/profile">
                                            <i class="bi bi-person me-2"></i> My Profile & Schedule
                                        </a>
                                    </li>
                                    <li>
                                        <a class="dropdown-item rounded py-2" href="${pageContext.request.contextPath}/credits">
                                            <i class="bi bi-wallet2 me-2"></i> Virtual Wallet (${sessionScope.user.creditBalance} pts)
                                        </a>
                                    </li>
                                    <li><hr class="dropdown-divider my-1"></li>
                                </c:if>
                                <li>
                                    <a class="dropdown-item rounded py-2 text-danger" href="${pageContext.request.contextPath}/logout">
                                        <i class="bi bi-box-arrow-right me-2"></i> Sign Out
                                    </a>
                                </li>
                            </ul>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/#how-it-works">How It Works</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/#popular-skills">Popular Skills</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/#benefits">Benefits</a>
                        </li>
                        <li class="nav-item ms-lg-2">
                            <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/login">Sign In</a>
                        </li>
                        <li class="nav-item ms-lg-1">
                            <a class="btn btn-primary" href="${pageContext.request.contextPath}/register">Join Platform</a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>

