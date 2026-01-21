-- =====================================================
-- ADMINS TABLE SQL SCRIPT
-- =====================================================

-- =====================================================
-- 1. CREATE TABLE
-- =====================================================

CREATE TABLE IF NOT EXISTS public.admins (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(512) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    name VARCHAR(255),
    role VARCHAR(20) NOT NULL DEFAULT 'ADMIN',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP
);

-- =====================================================
-- 2. INSERT DEFAULT ADMIN USER
-- =====================================================
-- Note: The password below is BCrypt encrypted

INSERT INTO admins (username, email, password, name, enabled, role)
VALUES (
    'pratik',
    'pratik@portfolio.com',
    '$2a$12$kHFd7M8RG/8Twu2E3YYclu5e1ERxk8396HZqofilTSpvvEQZRBUQ6',
    'System Administrator',
    TRUE,
    'ADMIN'
)
ON CONFLICT (username) DO NOTHING;

-- =====================================================
-- 3. GET
-- =====================================================

SELECT id, username, email, name, enabled, role, created_at, updated_at, last_login
FROM admins
WHERE username = 'admin';

SELECT id, username, email, name, enabled, role, created_at, updated_at, last_login
FROM admins
WHERE email = 'admin@portfolio.com';

SELECT id, username, email, name, enabled, role, created_at, updated_at, last_login
FROM admins
ORDER BY created_at DESC;

-- =====================================================
-- 4. UPDATE
-- =====================================================

UPDATE admins
SET email = 'newemail@portfolio.com',
    name = 'Updated Administrator',
    updated_at = CURRENT_TIMESTAMP
WHERE username = 'admin';

UPDATE admins
SET password = '$2a$12$VfNZqVRz0V6kZQzoG6D/UuJna0IGElJlf2GdVboWOZpppXkizJ0YG',
    updated_at = CURRENT_TIMESTAMP
WHERE username = 'admin';

UPDATE admins
SET last_login = CURRENT_TIMESTAMP
WHERE username = 'admin';

-- =====================================================
-- 5. DISABLE/ENABLE ADMIN ACCOUNT
-- =====================================================

UPDATE admins
SET enabled = FALSE,
    updated_at = CURRENT_TIMESTAMP
WHERE username = 'admin';

UPDATE admins
SET enabled = TRUE,
    updated_at = CURRENT_TIMESTAMP
WHERE username = 'admin';

-- =====================================================
-- INDEXES FOR PERFORMANCE
-- =====================================================

CREATE INDEX IF NOT EXISTS idx_admins_username ON admins(username);
CREATE INDEX IF NOT EXISTS idx_admins_email ON admins(email);
CREATE INDEX IF NOT EXISTS idx_admins_enabled ON admins(enabled);
CREATE INDEX IF NOT EXISTS idx_admins_created_at ON admins(created_at);

-- =====================================================
-- TRIGGER FOR AUTO-UPDATING
-- =====================================================

CREATE TRIGGER update_admins_updated_at
    BEFORE UPDATE ON admins
    FOR EACH ROW
    EXECUTE FUNCTION update_date_column();
