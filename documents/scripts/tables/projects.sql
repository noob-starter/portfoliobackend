-- =====================================================
-- PROJECTS TABLE SQL SCRIPT
-- =====================================================

-- =====================================================
-- 1. CREATE TABLE
-- =====================================================

CREATE TABLE IF NOT EXISTS public.projects (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    start_date DATE,
    end_date DATE,
    url VARCHAR(512),
    banner VARCHAR(512),
    github VARCHAR(512),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    profile_id BIGINT NOT NULL,
    CONSTRAINT fk_projects_profile FOREIGN KEY (profile_id) REFERENCES profiles(id) ON DELETE CASCADE
);

-- =====================================================
-- 2. CREATE PROJECT_POINTS TABLE
-- =====================================================

CREATE TABLE IF NOT EXISTS public.project_points (
    id BIGSERIAL PRIMARY KEY,
    content TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    project_id BIGINT NOT NULL,
    CONSTRAINT fk_project_points_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);

-- =====================================================
-- 3. CREATE JUNCTION TABLE (projects_technologies)
-- =====================================================

CREATE TABLE IF NOT EXISTS public.projects_technologies (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    project_id BIGINT NOT NULL,
    technology_id BIGINT NOT NULL,
    CONSTRAINT fk_proj_tech_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    CONSTRAINT fk_proj_tech_technology FOREIGN KEY (technology_id) REFERENCES technologies(id) ON DELETE CASCADE,
    CONSTRAINT uk_project_technology UNIQUE (project_id, technology_id)
);

-- =====================================================
-- 4. INSERT VALUES (Sample Data)
-- =====================================================

INSERT INTO projects (name, start_date, end_date, url, banner, github, profile_id)
VALUES (
    'E-Commerce Platform',
    '2023-01-01',
    '2023-06-30',
    'https://ecommerce-demo.com',
    'https://ecommerce-demo.com/banner.jpg',
    'https://github.com/pyawalkar/ecommerce',
    1
);

INSERT INTO project_points (content, project_id)
VALUES 
    ('Built a scalable microservices architecture', 1),
    ('Implemented secure payment gateway integration', 1),
    ('Developed responsive UI using React', 1);

INSERT INTO projects_technologies (project_id, technology_id)
VALUES 
    (1, 1),  -- Java
    (1, 2),  -- Spring Boot
    (1, 3);  -- React

-- =====================================================
-- 5. GET (Retrieve Single Project by ID with Details)
-- =====================================================

SELECT p.id, p.name, p.start_date, p.end_date, p.url, p.banner, p.github, p.profile_id, p.created_at, p.updated_at
FROM projects p
WHERE p.id = 1;

-- Get project points for a project
SELECT pp.id, pp.content, pp.project_id, pp.created_at, pp.updated_at
FROM project_points pp
WHERE pp.project_id = 1
ORDER BY pp.created_at ASC;

-- Get technologies for a project
SELECT t.id, t.name, t.category, t.proficiency
FROM technologies t
JOIN projects_technologies pt ON t.id = pt.technology_id
WHERE pt.project_id = 1;

-- =====================================================
-- 6. GET ALL (Retrieve All Projects)
-- =====================================================

SELECT id, name, start_date, end_date, url, banner, github, profile_id, created_at, updated_at
FROM projects
ORDER BY created_at DESC;

-- =====================================================
-- 7. GET BY PROFILE (Retrieve All Projects for Profile)
-- =====================================================

SELECT id, name, start_date, end_date, url, banner, github, profile_id, created_at, updated_at
FROM projects
WHERE profile_id = 1
ORDER BY start_date DESC;

-- =====================================================
-- 8. UPDATE (Update Project Information)
-- =====================================================

UPDATE projects
SET name = 'E-Commerce Platform V2',
    updated_at = CURRENT_TIMESTAMP
WHERE id = 1;

-- =====================================================
-- INDEXES FOR PERFORMANCE (Optional but recommended)
-- =====================================================

CREATE INDEX idx_projects_profile_id ON projects(profile_id);
CREATE INDEX idx_projects_start_date ON projects(start_date);
CREATE INDEX idx_projects_created_at ON projects(created_at);

CREATE INDEX idx_project_points_project_id ON project_points(project_id);
CREATE INDEX idx_project_points_created_at ON project_points(created_at);

CREATE INDEX idx_proj_tech_project_id ON projects_technologies(project_id);
CREATE INDEX idx_proj_tech_technology_id ON projects_technologies(technology_id);

-- =====================================================
-- TRIGGERS FOR AUTO-UPDATING 
-- =====================================================

CREATE TRIGGER update_projects_updated_at
    BEFORE UPDATE ON projects
    FOR EACH ROW
    EXECUTE FUNCTION update_date_column();

CREATE TRIGGER update_project_points_updated_at
    BEFORE UPDATE ON project_points
    FOR EACH ROW
    EXECUTE FUNCTION update_date_column();

CREATE TRIGGER update_projects_technologies_updated_at
    BEFORE UPDATE ON projects_technologies
    FOR EACH ROW
    EXECUTE FUNCTION update_date_column();

