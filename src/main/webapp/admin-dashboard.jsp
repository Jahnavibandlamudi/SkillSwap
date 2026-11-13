<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Administrator Dashboard" />
<c:set var="activePage" value="admin" />
<jsp:include page="/WEB-INF/partials/header.jsp" />
<jsp:include page="/WEB-INF/partials/navbar.jsp" />

<main class="py-4 bg-page">
    <div class="container">

        <!-- Flash messages -->
        <c:if test="${not empty flashSuccess}">
            <div class="alert alert-success alert-dismissible fade show small d-flex align-items-center gap-2 mb-4" role="alert">
                <i class="bi bi-check-circle-fill fs-5"></i>
                <div>${flashSuccess}</div>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger alert-dismissible fade show small d-flex align-items-center gap-2 mb-4" role="alert">
                <i class="bi bi-exclamation-triangle-fill fs-5"></i>
                <div>${errorMessage}</div>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <!-- Header -->
        <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3 mb-4">
            <div>
                <div class="d-inline-flex align-items-center gap-2 px-2 py-1 mb-1 rounded bg-dark text-white small fw-bold">
                    <i class="bi bi-shield-lock-fill text-warning"></i> Platform Administrator
                </div>
                <h3 class="fw-bold text-dark mb-0">System Operations &amp; Governance</h3>
                <p class="text-muted small mb-0">SRM University AP SkillSwap Central Control Console</p>
            </div>
            <div>
                <button type="button" class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#adminAddSkillModal">
                    <i class="bi bi-plus-lg me-1"></i> Add New Skill Category
                </button>
            </div>
        </div>

        <!-- 6 Metrics Cards -->
        <div class="row g-3 mb-4">
            <div class="col-lg-2 col-md-4 col-6">
                <div class="stat-card p-3">
                    <div class="stat-icon indigo mb-2"><i class="bi bi-people"></i></div>
                    <div class="stat-value fs-4">${stats.totalStudents}</div>
                    <div class="stat-label small">Total Students</div>
                </div>
            </div>
            <div class="col-lg-2 col-md-4 col-6">
                <div class="stat-card p-3">
                    <div class="stat-icon emerald mb-2"><i class="bi bi-person-check"></i></div>
                    <div class="stat-value fs-4">${stats.activeStudents}</div>
                    <div class="stat-label small">Active Students</div>
                </div>
            </div>
            <div class="col-lg-2 col-md-4 col-6">
                <div class="stat-card p-3">
                    <div class="stat-icon purple mb-2"><i class="bi bi-grid-3x3-gap"></i></div>
                    <div class="stat-value fs-4">${stats.totalSkills}</div>
                    <div class="stat-label small">Listed Skills</div>
                </div>
            </div>
            <div class="col-lg-2 col-md-4 col-6">
                <div class="stat-card p-3">
                    <div class="stat-icon amber mb-2"><i class="bi bi-arrow-repeat"></i></div>
                    <div class="stat-value fs-4">${stats.totalRequests}</div>
                    <div class="stat-label small">Total Requests</div>
                </div>
            </div>
            <div class="col-lg-2 col-md-4 col-6">
                <div class="stat-card p-3">
                    <div class="stat-icon emerald mb-2"><i class="bi bi-calendar-check"></i></div>
                    <div class="stat-value fs-4">${stats.completedSessions}</div>
                    <div class="stat-label small">Completed Sessions</div>
                </div>
            </div>
            <div class="col-lg-2 col-md-4 col-6">
                <div class="stat-card p-3">
                    <div class="stat-icon indigo mb-2"><i class="bi bi-star"></i></div>
                    <div class="stat-value fs-4">${stats.totalReviews}</div>
                    <div class="stat-label small">Total Reviews</div>
                </div>
            </div>
        </div>

        <!-- Admin Management Tabs -->
        <ul class="nav nav-pills mb-4 bg-white p-2 rounded-3 shadow-sm border" id="adminTabs" role="tablist">
            <li class="nav-item" role="presentation">
                <button class="nav-link active fw-bold small" id="students-tab" data-bs-toggle="pill" data-bs-target="#students" type="button" role="tab">
                    <i class="bi bi-people me-1"></i> Student Registry (${students.size()})
                </button>
            </li>
            <li class="nav-item" role="presentation">
                <button class="nav-link fw-bold small" id="skills-tab" data-bs-toggle="pill" data-bs-target="#skillsAdmin" type="button" role="tab">
                    <i class="bi bi-grid me-1"></i> Skill Catalog (${skills.size()})
                </button>
            </li>
            <li class="nav-item" role="presentation">
                <button class="nav-link fw-bold small" id="activity-tab" data-bs-toggle="pill" data-bs-target="#activity" type="button" role="tab">
                    <i class="bi bi-clock-history me-1"></i> Platform Exchange Audit
                </button>
            </li>
        </ul>

        <div class="tab-content" id="adminTabsContent">
            
            <!-- 1. Student Registry Tab -->
            <div class="tab-pane fade show active" id="students" role="tabpanel">
                <div class="card-custom bg-white p-3">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <h6 class="fw-bold text-dark mb-0">Registered SRM University AP Students</h6>
                        <input type="text" id="liveSearchInput" class="form-control form-control-sm w-auto" placeholder="Live search students...">
                    </div>
                    <div class="table-responsive">
                        <table class="table table-hover align-middle small mb-0">
                            <thead class="table-light">
                                <tr>
                                    <th>Student ID</th>
                                    <th>Full Name</th>
                                    <th>College Email</th>
                                    <th>Department</th>
                                    <th>Year</th>
                                    <th>Status</th>
                                    <th class="text-end">Account Control</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="std" items="${students}">
                                    <tr class="searchable-item">
                                        <td class="fw-bold">${std.studentId}</td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/profile?id=${std.userId}" class="text-dark fw-semibold text-decoration-none">
                                                ${std.fullName}
                                            </a>
                                        </td>
                                        <td>${std.email}</td>
                                        <td>${std.department}</td>
                                        <td>Year ${std.yearOfStudy}</td>
                                        <td>
                                            <span class="badge ${std.active ? 'bg-success-subtle text-success border border-success-subtle' : 'bg-danger-subtle text-danger border border-danger-subtle'}">
                                                ${std.status}
                                            </span>
                                        </td>
                                        <td class="text-end">
                                            <form action="${pageContext.request.contextPath}/admin" method="POST" class="d-inline"
                                                  onsubmit="return confirm('Toggle status for ${std.fullName}?');">
                                                <input type="hidden" name="action" value="toggleStatus">
                                                <input type="hidden" name="userId" value="${std.userId}">
                                                <input type="hidden" name="newStatus" value="${std.active ? 'INACTIVE' : 'ACTIVE'}">
                                                <button type="submit" class="btn btn-sm ${std.active ? 'btn-outline-danger' : 'btn-outline-success'} py-0 px-2">
                                                    <i class="bi ${std.active ? 'bi-person-x' : 'bi-person-check'}"></i>
                                                    ${std.active ? 'Deactivate' : 'Activate'}
                                                </button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <!-- 2. Skill Catalog Management Tab -->
            <div class="tab-pane fade" id="skillsAdmin" role="tabpanel">
                <div class="card-custom bg-white p-3">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <h6 class="fw-bold text-dark mb-0">Master Skills &amp; Categories Catalog</h6>
                        <button type="button" class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#adminAddSkillModal">
                            <i class="bi bi-plus-lg me-1"></i> Add Skill
                        </button>
                    </div>
                    <div class="table-responsive">
                        <table class="table table-hover align-middle small mb-0">
                            <thead class="table-light">
                                <tr>
                                    <th>ID</th>
                                    <th>Skill Name</th>
                                    <th>Category</th>
                                    <th>Description</th>
                                    <th>Teachers</th>
                                    <th>Learners</th>
                                    <th class="text-end">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="sk" items="${skills}">
                                    <tr>
                                        <td>#${sk.skillId}</td>
                                        <td class="fw-bold text-dark">${sk.skillName}</td>
                                        <td><span class="badge bg-light text-secondary border">${sk.category}</span></td>
                                        <td class="text-muted">${sk.description}</td>
                                        <td><span class="badge badge-custom badge-offer">${sk.offeredCount}</span></td>
                                        <td><span class="badge badge-custom badge-want">${sk.wantedCount}</span></td>
                                        <td class="text-end">
                                            <form action="${pageContext.request.contextPath}/admin" method="POST" class="d-inline"
                                                  onsubmit="return confirm('Delete ${sk.skillName} from catalog?');">
                                                <input type="hidden" name="action" value="deleteSkill">
                                                <input type="hidden" name="skillId" value="${sk.skillId}">
                                                <button type="submit" class="btn btn-sm btn-outline-danger py-0 px-2" title="Delete">
                                                    <i class="bi bi-trash"></i>
                                                </button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <!-- 3. Platform Exchange Audit Tab -->
            <div class="tab-pane fade" id="activity" role="tabpanel">
                <div class="row g-4">
                    <div class="col-lg-6">
                        <div class="card-custom bg-white p-3">
                            <h6 class="fw-bold text-dark mb-3">Recent Skill Exchange Proposals</h6>
                            <div class="table-responsive">
                                <table class="table table-hover align-middle small mb-0">
                                    <thead class="table-light">
                                        <tr>
                                            <th>Sender</th>
                                            <th>Receiver</th>
                                            <th>Exchange</th>
                                            <th>Status</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="req" items="${requests}">
                                            <tr>
                                                <td class="fw-semibold">${req.senderName}</td>
                                                <td class="fw-semibold">${req.receiverName}</td>
                                                <td>${req.offeredSkillName} &harr; ${req.requestedSkillName}</td>
                                                <td>
                                                    <span class="badge ${req.accepted ? 'bg-success' : (req.pending ? 'bg-warning text-dark' : 'bg-secondary')}">
                                                        ${req.status}
                                                    </span>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>

                    <div class="col-lg-6">
                        <div class="card-custom bg-white p-3">
                            <h6 class="fw-bold text-dark mb-3">Recent Learning Sessions</h6>
                            <div class="table-responsive">
                                <table class="table table-hover align-middle small mb-0">
                                    <thead class="table-light">
                                        <tr>
                                            <th>Skill</th>
                                            <th>Teacher</th>
                                            <th>Learner</th>
                                            <th>Status</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="sess" items="${sessions}">
                                            <tr>
                                                <td class="fw-bold">${sess.skillName}</td>
                                                <td>${sess.teacherName}</td>
                                                <td>${sess.learnerName}</td>
                                                <td>
                                                    <span class="badge ${sess.completed ? 'bg-success' : 'bg-primary'}">
                                                        ${sess.status}
                                                    </span>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </div>

    </div>
</main>

<!-- Add Skill Modal -->
<div class="modal fade" id="adminAddSkillModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content border-0 shadow">
            <div class="modal-header border-bottom">
                <h5 class="modal-title fw-bold">Add Skill to Platform Catalog</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <form action="${pageContext.request.contextPath}/admin" method="POST">
                <input type="hidden" name="action" value="addSkill">
                <div class="modal-body">
                    <div class="mb-3">
                        <label class="form-label" for="adminSkillName">Skill Name <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="adminSkillName" name="skillName"
                               placeholder="e.g. Flutter Development, Cloud Computing" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label" for="adminCategory">Category <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="adminCategory" name="category" list="categoryOptions"
                               placeholder="Select or type new category..." required>
                        <datalist id="categoryOptions">
                            <c:forEach var="cat" items="${categories}">
                                <option value="${cat}">${cat}</option>
                            </c:forEach>
                        </datalist>
                    </div>
                    <div class="mb-3">
                        <label class="form-label" for="adminDescription">Description</label>
                        <textarea class="form-control" id="adminDescription" name="description" rows="3"
                                  placeholder="Short overview of topics covered under this skill."></textarea>
                    </div>
                </div>
                <div class="modal-footer border-top">
                    <button type="button" class="btn btn-subtle" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary">Save Skill</button>
                </div>
            </form>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/partials/footer.jsp" />

