-- =====================================================
-- INQUIRES TABLE SQL SCRIPT
-- =====================================================

-- =====================================================
-- 1. CREATE TABLE
-- =====================================================

CREATE TABLE IF NOT EXISTS public.inquires (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    message TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    profile_id SERIAL NOT NULL,
    CONSTRAINT fk_inquires_profile FOREIGN KEY (profile_id) REFERENCES profiles(id) ON DELETE CASCADE
);

-- =====================================================
-- 2. INSERT VALUES (Sample Data)
-- =====================================================

INSERT INTO inquires (name, email, message, profile_id)
VALUES (
    'John Doe',
    'john.doe@example.com',
    'I would like to discuss a potential project opportunity.',
    1
);

-- =====================================================
-- 3. GET (Retrieve Single Inquire by ID)
-- =====================================================

SELECT id, name, email, message, profile_id, created_at, updated_at
FROM inquires
WHERE id = 1;

-- =====================================================
-- 4. GET ALL (Retrieve All Inquires)
-- =====================================================

SELECT id, name, email, message, profile_id, created_at, updated_at
FROM inquires
ORDER BY created_at DESC;

-- =====================================================
-- 5. GET BY PROFILE (Retrieve All Inquires for Profile)
-- =====================================================

SELECT id, name, email, message, profile_id, created_at, updated_at
FROM inquires
WHERE profile_id = 1
ORDER BY created_at DESC;

-- =====================================================
-- 6. UPDATE (Update Inquire Information)
-- =====================================================

UPDATE inquires
SET message = 'Updated message content',
    updated_at = CURRENT_TIMESTAMP
WHERE id = 1;

-- =====================================================
-- INDEXES FOR PERFORMANCE (Optional but recommended)
-- =====================================================

CREATE INDEX idx_inquires_profile_id ON inquires(profile_id);
CREATE INDEX idx_inquires_created_at ON inquires(created_at);
CREATE INDEX idx_inquires_email ON inquires(email);

-- =====================================================
-- TRIGGER FOR AUTO-UPDATING 
-- =====================================================

CREATE TRIGGER update_inquires_updated_at
    BEFORE UPDATE ON inquires
    FOR EACH ROW
    EXECUTE FUNCTION update_date_column();

