-- =====================================================
-- EDUCATIONS TABLE SQL SCRIPT
-- =====================================================

-- =====================================================
-- 1. CREATE TABLE
-- =====================================================

CREATE TABLE IF NOT EXISTS public.educations (
    id BIGSERIAL PRIMARY KEY,
    degree VARCHAR(255),
    institution VARCHAR(255),
    field VARCHAR(255),
    start_date DATE,
    end_date DATE,
    percentage NUMERIC(5,2),
    description TEXT,
    url VARCHAR(512),
    banner VARCHAR(512),
    github VARCHAR(512),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    profile_id BIGINT NOT NULL,
    CONSTRAINT fk_educations_profile FOREIGN KEY (profile_id) REFERENCES profiles(id) ON DELETE CASCADE
);

-- =====================================================
-- 2. INSERT VALUES (Sample Data)
-- =====================================================

INSERT INTO educations (degree, institution, field, start_date, end_date, percentage, description, url, banner, github, profile_id)
VALUES (
    'Bachelor of Engineering',
    'University of Mumbai',
    'Computer Science',
    '2018-08-01',
    '2022-06-30',
    85.50,
    'Focused on software engineering, algorithms, and data structures',
    'https://mu.ac.in',
    'https://mu.ac.in/banner.jpg',
    'https://github.com/university',
    1
);

-- =====================================================
-- 3. GET (Retrieve Single Education by ID)
-- =====================================================

SELECT id, degree, institution, field, start_date, end_date, percentage, description, url, banner, github, profile_id, created_at, updated_at
FROM educations
WHERE id = 1;

-- =====================================================
-- 4. GET ALL (Retrieve All Educations)
-- =====================================================

SELECT id, degree, institution, field, start_date, end_date, percentage, description, url, banner, github, profile_id, created_at, updated_at
FROM educations
ORDER BY created_at DESC;

-- =====================================================
-- 5. GET BY PROFILE (Retrieve All Educations for Profile)
-- =====================================================

SELECT id, degree, institution, field, start_date, end_date, percentage, description, url, banner, github, profile_id, created_at, updated_at
FROM educations
WHERE profile_id = 1
ORDER BY start_date DESC;

-- =====================================================
-- 6. UPDATE (Update Education Information)
-- =====================================================

UPDATE educations
SET percentage = 87.00,
    updated_at = CURRENT_TIMESTAMP
WHERE id = 1;

-- =====================================================
-- INDEXES FOR PERFORMANCE (Optional but recommended)
-- =====================================================

CREATE INDEX idx_educations_profile_id ON educations(profile_id);
CREATE INDEX idx_educations_start_date ON educations(start_date);
CREATE INDEX idx_educations_created_at ON educations(created_at);

-- =====================================================
-- TRIGGER FOR AUTO-UPDATING 
-- =====================================================

CREATE TRIGGER update_educations_updated_at
    BEFORE UPDATE ON educations
    FOR EACH ROW
    EXECUTE FUNCTION update_date_column();

