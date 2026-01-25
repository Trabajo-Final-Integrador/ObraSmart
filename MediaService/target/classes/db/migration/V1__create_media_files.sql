create table media_files (
  id uuid primary key,
  owner_type varchar(80) not null,
  owner_id varchar(120) not null,
  purpose varchar(80) not null,
  original_name varchar(255) not null,
  content_type varchar(120) not null,
  size bigint not null,
  path varchar(500) not null,
  is_active boolean not null default true,
  created_at timestamp with time zone not null default now()
);

create index idx_media_owner on media_files (owner_type, owner_id, purpose, is_active);

create table equipment_images (
  id uuid primary key,
  equipo_id varchar(120) not null unique,
  source_type varchar(20) not null,
  file_name varchar(255),
  content_type varchar(120),
  size bigint,
  relative_path varchar(500),
  external_url varchar(500),
  updated_at timestamp with time zone not null default now()
);
