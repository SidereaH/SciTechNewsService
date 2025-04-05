CREATE DATABASE scitech_newsservice;

-- Create tables
CREATE TABLE news_tag (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE status (
                        id BIGSERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE news_object (
                             id BIGSERIAL PRIMARY KEY,
                             owner_id BIGINT NOT NULL,
                             theme VARCHAR(255),
                             status_id BIGINT,
                             title VARCHAR(255) NOT NULL,
                             description TEXT,
                             content TEXT,
                             url VARCHAR(512),
                             FOREIGN KEY (status_id) REFERENCES status(id)
);

CREATE TABLE news_object_tags (
                                  news_object_id BIGINT NOT NULL,
                                  tags_id BIGINT NOT NULL,
                                  PRIMARY KEY (news_object_id, tags_id),
                                  FOREIGN KEY (news_object_id) REFERENCES news_object(id),
                                  FOREIGN KEY (tags_id) REFERENCES news_tag(id)
);

-- Insert initial statuses
INSERT INTO status (name) VALUES
                              ('DRAFT'),
                              ('PUBLISHED'),
                              ('ARCHIVED'),
                              ('DELETED');

-- Insert initial tags
INSERT INTO news_tag (name) VALUES
                                ('Politics'),
                                ('Technology'),
                                ('Science'),
                                ('Health'),
                                ('Business'),
                                ('Entertainment'),
                                ('Sports'),
                                ('Education'),
                                ('Environment'),
                                ('World');

-- Insert sample news objects
INSERT INTO news_object (owner_id, theme, status_id, title, description, content, url) VALUES
                                                                                           (1, 'Technology', 2, 'New AI Breakthrough', 'Researchers develop new AI model', 'Long content about AI...', 'https://example.com/ai-breakthrough'),
                                                                                           (2, 'Health', 2, 'COVID-19 Update', 'Latest developments in pandemic', 'Content about COVID...', 'https://example.com/covid-update'),
                                                                                           (1, 'Politics', 1, 'Election Results Draft', 'Preliminary election results', 'Draft content...', NULL);

-- Assign tags to news objects
INSERT INTO news_object_tags (news_object_id, tags_id) VALUES
                                                           (1, 2),  -- AI article tagged with Technology
                                                           (1, 3),  -- AI article tagged with Science
                                                           (2, 4),  -- COVID article tagged with Health
                                                           (2, 10), -- COVID article tagged with World
                                                           (3, 1);  -- Election article tagged with Politics

CREATE INDEX idx_news_created_at ON news_object(created_at);
CREATE INDEX idx_news_owner ON news_object(owner_id);
CREATE INDEX idx_news_theme ON news_object(theme);