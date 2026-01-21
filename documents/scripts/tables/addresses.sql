-- =====================================================
-- ADDRESSES TABLE SQL SCRIPT
-- =====================================================

-- =====================================================
-- 1. CREATE TABLE
-- =====================================================

CREATE TABLE IF NOT EXISTS public.addresses (
    id BIGSERIAL PRIMARY KEY,
    street VARCHAR(255),
    landmark VARCHAR(255),
    city VARCHAR(64),
    state VARCHAR(64),
    country VARCHAR(64),
    pincode NUMERIC,
    type VARCHAR(64),
    phone VARCHAR(16),
    email VARCHAR(255),
    url VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    profile_id BIGINT NOT NULL,
    CONSTRAINT fk_addresses_profile FOREIGN KEY (profile_id) REFERENCES profiles(id) ON DELETE CASCADE
);

-- =====================================================
-- 2. INSERT VALUES (Sample Data)
-- =====================================================

INSERT INTO addresses (street, landmark, city, state, country, pincode, type, phone, email, url, profile_id)
VALUES (
    '123 Main Street',
    'Near Central Park',
    'Mumbai',
    'Maharashtra',
    'India',
    400001,
    'Home',
    '+91-9876543210',
    'pratik@example.com',
    'https://example.com',
    1
);

-- =====================================================
-- 3. GET (Retrieve Single Address by ID)
-- =====================================================

SELECT id, street, landmark, city, state, country, pincode, type, phone, email, url, profile_id, created_at, updated_at
FROM addresses
WHERE id = 1;

-- =====================================================
-- 4. GET ALL (Retrieve All Addresses)
-- =====================================================

SELECT id, street, landmark, city, state, country, pincode, type, phone, email, url, profile_id, created_at, updated_at
FROM addresses
ORDER BY created_at DESC;

-- =====================================================
-- 5. GET BY PROFILE (Retrieve All Addresses for Profile)
-- =====================================================

SELECT id, street, landmark, city, state, country, pincode, type, phone, email, url, profile_id, created_at, updated_at
FROM addresses
WHERE profile_id = 1
ORDER BY created_at DESC;

-- =====================================================
-- 6. UPDATE (Update Address Information)
-- =====================================================

UPDATE addresses
SET city = 'New Delhi',
    state = 'Delhi',
    updated_at = CURRENT_TIMESTAMP
WHERE id = 1;

-- =====================================================
-- INDEXES FOR PERFORMANCE (Optional but recommended)
-- =====================================================

CREATE INDEX idx_addresses_profile_id ON addresses(profile_id);
CREATE INDEX idx_addresses_created_at ON addresses(created_at);

-- =====================================================
-- TRIGGER FOR AUTO-UPDATING 
-- =====================================================

CREATE TRIGGER update_addresses_updated_at
    BEFORE UPDATE ON addresses
    FOR EACH ROW
    EXECUTE FUNCTION update_date_column();

