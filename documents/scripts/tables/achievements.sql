-- =====================================================
-- ACHIEVEMENTS TABLE SQL SCRIPT
-- =====================================================

-- =====================================================
-- 1. CREATE TABLE
-- =====================================================

CREATE TABLE IF NOT EXISTS public.achievements (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    date_achieved DATE,
    issuer VARCHAR(255),
    description TEXT,
    url VARCHAR(512),
    banner VARCHAR(512),
    github VARCHAR(512),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    profile_id SERIAL NOT NULL,
    CONSTRAINT fk_achievements_profile FOREIGN KEY (profile_id) REFERENCES profiles(id) ON DELETE CASCADE
);

-- =====================================================
-- 2. INSERT VALUES (Sample Data)
-- =====================================================

INSERT INTO achievements (name, date_achieved, issuer, description, url, banner, github, profile_id)
VALUES (
    'AWS Certified Solutions Architect',
    '2023-06-15',
    'Amazon Web Services',
    'Successfully completed AWS Solutions Architect Associate certification',
    'https://aws.amazon.com/certification',
    'https://aws.amazon.com/certification/banner.jpg',
    'https://github.com/aws-samples',
    1
);

-- =====================================================
-- 3. GET (Retrieve Single Achievement by ID)
-- =====================================================

SELECT id, name, date_achieved, issuer, description, url, banner, github, profile_id, created_at, updated_at
FROM achievements
WHERE id = 1;

-- =====================================================
-- 4. GET ALL (Retrieve All Achievements)
-- =====================================================

SELECT id, name, date_achieved, issuer, description, url, banner, github, profile_id, created_at, updated_at
FROM achievements
ORDER BY created_at DESC;

-- =====================================================
-- 5. GET BY PROFILE (Retrieve All Achievements for Profile)
-- =====================================================

SELECT id, name, date_achieved, issuer, description, url, banner, github, profile_id, created_at, updated_at
FROM achievements
WHERE profile_id = 1
ORDER BY date_achieved DESC;

-- =====================================================
-- 6. UPDATE (Update Achievement Information)
-- =====================================================

UPDATE achievements
SET description = 'Successfully completed AWS Solutions Architect Associate certification with distinction',
    updated_at = CURRENT_TIMESTAMP
WHERE id = 1;

-- =====================================================
-- INDEXES FOR PERFORMANCE (Optional but recommended)
-- =====================================================

CREATE INDEX idx_achievements_profile_id ON achievements(profile_id);
CREATE INDEX idx_achievements_date_achieved ON achievements(date_achieved);
CREATE INDEX idx_achievements_created_at ON achievements(created_at);

-- =====================================================
-- TRIGGER FOR AUTO-UPDATING 
-- =====================================================

CREATE TRIGGER update_achievements_updated_at
    BEFORE UPDATE ON achievements
    FOR EACH ROW
    EXECUTE FUNCTION update_date_column();

