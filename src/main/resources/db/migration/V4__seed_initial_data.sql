INSERT INTO disposable_domain (domain) SELECT 'tempmail.com' WHERE NOT EXISTS (SELECT 1 FROM disposable_domain WHERE domain = 'tempmail.com');
INSERT INTO disposable_domain (domain) SELECT 'guerrillamail.com' WHERE NOT EXISTS (SELECT 1 FROM disposable_domain WHERE domain = 'guerrillamail.com');
INSERT INTO disposable_domain (domain) SELECT '10minutemail.com' WHERE NOT EXISTS (SELECT 1 FROM disposable_domain WHERE domain = '10minutemail.com');
INSERT INTO disposable_domain (domain) SELECT 'throwaway.email' WHERE NOT EXISTS (SELECT 1 FROM disposable_domain WHERE domain = 'throwaway.email');
INSERT INTO disposable_domain (domain) SELECT 'mailinator.com' WHERE NOT EXISTS (SELECT 1 FROM disposable_domain WHERE domain = 'mailinator.com');
INSERT INTO disposable_domain (domain) SELECT 'yopmail.com' WHERE NOT EXISTS (SELECT 1 FROM disposable_domain WHERE domain = 'yopmail.com');
INSERT INTO disposable_domain (domain) SELECT 'temp-mail.org' WHERE NOT EXISTS (SELECT 1 FROM disposable_domain WHERE domain = 'temp-mail.org');
INSERT INTO disposable_domain (domain) SELECT 'getnada.com' WHERE NOT EXISTS (SELECT 1 FROM disposable_domain WHERE domain = 'getnada.com');
INSERT INTO disposable_domain (domain) SELECT 'mohmal.com' WHERE NOT EXISTS (SELECT 1 FROM disposable_domain WHERE domain = 'mohmal.com');
INSERT INTO disposable_domain (domain) SELECT 'fakeinbox.com' WHERE NOT EXISTS (SELECT 1 FROM disposable_domain WHERE domain = 'fakeinbox.com');
INSERT INTO disposable_domain (domain) SELECT 'trashmail.com' WHERE NOT EXISTS (SELECT 1 FROM disposable_domain WHERE domain = 'trashmail.com');
INSERT INTO disposable_domain (domain) SELECT 'maildrop.cc' WHERE NOT EXISTS (SELECT 1 FROM disposable_domain WHERE domain = 'maildrop.cc');
INSERT INTO disposable_domain (domain) SELECT 'meltmail.com' WHERE NOT EXISTS (SELECT 1 FROM disposable_domain WHERE domain = 'meltmail.com');
INSERT INTO disposable_domain (domain) SELECT 'sharklasers.com' WHERE NOT EXISTS (SELECT 1 FROM disposable_domain WHERE domain = 'sharklasers.com');
INSERT INTO disposable_domain (domain) SELECT 'grr.la' WHERE NOT EXISTS (SELECT 1 FROM disposable_domain WHERE domain = 'grr.la');
INSERT INTO disposable_domain (domain) SELECT 'spam4.me' WHERE NOT EXISTS (SELECT 1 FROM disposable_domain WHERE domain = 'spam4.me');
INSERT INTO disposable_domain (domain) SELECT 'dispostable.com' WHERE NOT EXISTS (SELECT 1 FROM disposable_domain WHERE domain = 'dispostable.com');
INSERT INTO disposable_domain (domain) SELECT 'mintemail.com' WHERE NOT EXISTS (SELECT 1 FROM disposable_domain WHERE domain = 'mintemail.com');
INSERT INTO disposable_domain (domain) SELECT 'mytrashmail.com' WHERE NOT EXISTS (SELECT 1 FROM disposable_domain WHERE domain = 'mytrashmail.com');
INSERT INTO disposable_domain (domain) SELECT 'tempail.com' WHERE NOT EXISTS (SELECT 1 FROM disposable_domain WHERE domain = 'tempail.com');
INSERT INTO disposable_domain (domain) SELECT 'emailondeck.com' WHERE NOT EXISTS (SELECT 1 FROM disposable_domain WHERE domain = 'emailondeck.com');
INSERT INTO disposable_domain (domain) SELECT 'throwawaymail.com' WHERE NOT EXISTS (SELECT 1 FROM disposable_domain WHERE domain = 'throwawaymail.com');
INSERT INTO disposable_domain (domain) SELECT 'tempinbox.co.uk' WHERE NOT EXISTS (SELECT 1 FROM disposable_domain WHERE domain = 'tempinbox.co.uk');
INSERT INTO disposable_domain (domain) SELECT 'mailcatch.com' WHERE NOT EXISTS (SELECT 1 FROM disposable_domain WHERE domain = 'mailcatch.com');
INSERT INTO disposable_domain (domain) SELECT 'inboxkitten.com' WHERE NOT EXISTS (SELECT 1 FROM disposable_domain WHERE domain = 'inboxkitten.com');
INSERT INTO disposable_domain (domain) SELECT 'tempr.email' WHERE NOT EXISTS (SELECT 1 FROM disposable_domain WHERE domain = 'tempr.email');
INSERT INTO disposable_domain (domain) SELECT 'emailfake.com' WHERE NOT EXISTS (SELECT 1 FROM disposable_domain WHERE domain = 'emailfake.com');
INSERT INTO disposable_domain (domain) SELECT 'temp-mail.io' WHERE NOT EXISTS (SELECT 1 FROM disposable_domain WHERE domain = 'temp-mail.io');
INSERT INTO disposable_domain (domain) SELECT 'temp-mail.ru' WHERE NOT EXISTS (SELECT 1 FROM disposable_domain WHERE domain = 'temp-mail.ru');

INSERT INTO admin_user (username, password_hash, role, active) 
SELECT 'admin', '$2y$10$VeO5ermYKP51o3HuyYx3AugI5.slEfLif9FSgz95NjaQtOMYz22KW', 'ROLE_ADMIN', TRUE
WHERE NOT EXISTS (SELECT 1 FROM admin_user WHERE username = 'admin');
