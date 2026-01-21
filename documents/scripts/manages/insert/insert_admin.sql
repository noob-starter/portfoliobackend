-- =====================================================
-- COMPLETE ADMIN TABLE INSERTION SCRIPT
-- =====================================================

INSERT INTO admins (username, email, password, name, enabled, role)
VALUES (
           'pratik',
           'pratikyawalkar71@gmail.com',
           '$2a$12$D.YeMfcI8Cb4kxJvBZt4I.37gU5E4AsUzEXqe4nETnlLFQOGQ.FbC',
           'System Administrator',
           TRUE,
           'ADMIN'
       )
    ON CONFLICT (username) DO NOTHING;


INSERT INTO admins (username, email, password, name, enabled, role)
VALUES (
           'admin',
           'admin@portfolio.com',
           '$2a$12$Lh6Fvga6kKi/iIRn.uqKueJJ2sTbcGEbmbwW8quaGRxdruARdT23u',
           'System Administrator',
           TRUE,
           'ADMIN'
       )
    ON CONFLICT (username) DO NOTHING;