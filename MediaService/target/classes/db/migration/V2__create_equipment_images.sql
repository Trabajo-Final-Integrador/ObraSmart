CREATE TABLE IF NOT EXISTS equipment_images (
    id UUID PRIMARY KEY,
    equipo_id VARCHAR(255) NOT NULL UNIQUE,
    source_type VARCHAR(20) NOT NULL,
    file_name VARCHAR(512),
    content_type VARCHAR(255),
    size BIGINT,
    relative_path VARCHAR(1024),
    external_url VARCHAR(1024),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL
);
