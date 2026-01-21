-- =====================================================
-- FAQS TABLE SQL SCRIPT
-- =====================================================

-- =====================================================
-- 1. CREATE TABLE
-- =====================================================

CREATE TABLE IF NOT EXISTS public.faqs (
    id SERIAL PRIMARY KEY,
    question TEXT NOT NULL,
    answer TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    profile_id SERIAL NOT NULL,
    CONSTRAINT fk_faqs_profile FOREIGN KEY (profile_id) REFERENCES profiles(id) ON DELETE CASCADE
);

-- =====================================================
-- 2. INSERT VALUES (Sample Data)
-- =====================================================

INSERT INTO faqs (question, answer, profile_id)
VALUES (
    'What technologies do you specialize in?',
    'I specialize in Java, Spring Boot, React, and PostgreSQL. I have extensive experience building full-stack web applications with modern frameworks and best practices.',
    1
);

INSERT INTO faqs (question, answer, profile_id)
VALUES (
    'How many years of experience do you have?',
    'I have over 5 years of professional software development experience, working on various projects ranging from small startups to enterprise-level applications.',
    1
);

-- =====================================================
-- 3. GET (Retrieve Single FAQ by ID)
-- =====================================================

SELECT id, question, answer, profile_id, created_at, updated_at
FROM faqs
WHERE id = 1;

-- =====================================================
-- 4. GET ALL (Retrieve All FAQs)
-- =====================================================

SELECT id, question, answer, profile_id, created_at, updated_at
FROM faqs
ORDER BY created_at DESC;

-- =====================================================
-- 5. GET BY PROFILE (Retrieve All FAQs for Profile)
-- =====================================================

SELECT id, question, answer, profile_id, created_at, updated_at
FROM faqs
WHERE profile_id = 1
ORDER BY created_at DESC;

-- =====================================================
-- 6. UPDATE (Update FAQ Information)
-- =====================================================

UPDATE faqs
SET answer = 'I specialize in Java, Spring Boot, React, PostgreSQL, and Docker. I have extensive experience building full-stack web applications with modern frameworks, microservices architecture, and best practices.',
    updated_at = CURRENT_TIMESTAMP
WHERE id = 1;

-- =====================================================
-- INDEXES FOR PERFORMANCE (Optional but recommended)
-- =====================================================

CREATE INDEX idx_faqs_profile_id ON faqs(profile_id);
CREATE INDEX idx_faqs_created_at ON faqs(created_at);

-- =====================================================
-- TRIGGER FOR AUTO-UPDATING 
-- =====================================================

CREATE TRIGGER update_faqs_updated_at
    BEFORE UPDATE ON faqs
    FOR EACH ROW
    EXECUTE FUNCTION update_date_column();


