-- ========================================
-- Table: app_user
-- ========================================
CREATE TABLE app_user (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    phone VARCHAR(50),
    password VARCHAR(255)
);

-- A table for favorite building IDs (from @ElementCollection)
CREATE TABLE app_user_favorite_building_ids (
    app_user_id BIGINT NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
    favorite_building_ids BIGINT,
    PRIMARY KEY (app_user_id, favorite_building_ids)
);

-- ========================================
-- Table: building
-- ========================================
CREATE TABLE building (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    description TEXT,
    cost DOUBLE PRECISION,
    type VARCHAR(100),
    location VARCHAR(255),
    available BOOLEAN
);

-- ElementCollection tables for Building
CREATE TABLE building_images (
    building_id BIGINT NOT NULL REFERENCES building(id) ON DELETE CASCADE,
    images VARCHAR(255),
    PRIMARY KEY (building_id, images)
);

CREATE TABLE building_price_history (
    building_id BIGINT NOT NULL REFERENCES building(id) ON DELETE CASCADE,
    price_history DOUBLE PRECISION,
    PRIMARY KEY (building_id, price_history)
);

-- ========================================
-- Table: appointment
-- ========================================
CREATE TABLE appointment (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
    building_id BIGINT NOT NULL REFERENCES building(id) ON DELETE CASCADE,
    appointment_time VARCHAR(100) NOT NULL,
    verified BOOLEAN NOT NULL DEFAULT FALSE,
    mode VARCHAR(50) NOT NULL DEFAULT 'physical',
    status VARCHAR(50) DEFAULT 'pending',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
