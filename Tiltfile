docker_compose('docker-compose.yaml')
# Add labels to Docker services
dc_resource('mongodb', labels=["database"])

# Frontend Configuration
local_resource(
  'frontend_dependencies',
  cmd='cd nonsqllogdb-dashboard && npm install',
  labels=['dependencies'],
  deps=['./frontend/'],
)
local_resource(
  'frontend',
  serve_cmd='cd nonsqllogdb-dashboard && npm start',
  labels=['frontend'],
  resource_deps=['backend', 'frontend_dependencies']
)