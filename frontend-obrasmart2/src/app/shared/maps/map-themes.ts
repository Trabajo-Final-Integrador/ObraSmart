import * as L from 'leaflet';

export interface MapTheme {
  id: string;
  labelKey: string;
  tileUrl: string;
  attribution: string;
  maxZoom?: number;
  subdomains?: string[];
}

export const MAP_THEMES: MapTheme[] = [
  {
    id: 'osm-classic',
    labelKey: 'navbar.map.themes.osm-classic',
    tileUrl: 'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
    attribution: '&copy; OpenStreetMap contributors',
    subdomains: ['a', 'b', 'c']
  },
  {
    id: 'carto-positron',
    labelKey: 'navbar.map.themes.carto-positron',
    tileUrl: 'https://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}{r}.png',
    attribution: '&copy; OpenStreetMap contributors &copy; CARTO',
    subdomains: ['a', 'b', 'c', 'd']
  },
  {
    id: 'carto-darkmatter',
    labelKey: 'navbar.map.themes.carto-darkmatter',
    tileUrl: 'https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png',
    attribution: '&copy; OpenStreetMap contributors &copy; CARTO',
    subdomains: ['a', 'b', 'c', 'd']
  },
  {
    id: 'carto-voyager',
    labelKey: 'navbar.map.themes.carto-voyager',
    tileUrl: 'https://{s}.basemaps.cartocdn.com/rastertiles/voyager/{z}/{x}/{y}{r}.png',
    attribution: '&copy; OpenStreetMap &copy; CARTO',
    subdomains: ['a', 'b', 'c', 'd'],
    maxZoom: 19
  },
  {
    id: 'opentopo',
    labelKey: 'navbar.map.themes.opentopo',
    tileUrl: 'https://{s}.tile.opentopomap.org/{z}/{x}/{y}.png',
    attribution: 'Map data: &copy; OpenStreetMap contributors, SRTM | Map style: &copy; OpenTopoMap',
    subdomains: ['a', 'b', 'c'],
    maxZoom: 17
  },
  {
    id: 'osm-hot',
    labelKey: 'navbar.map.themes.osm-hot',
    tileUrl: 'https://{s}.tile.openstreetmap.fr/hot/{z}/{x}/{y}.png',
    attribution: '&copy; OpenStreetMap contributors, Humanitarian OpenStreetMap Team',
    subdomains: ['a', 'b', 'c']
  }
];

export const DEFAULT_THEME_ID = 'carto-positron';

export function buildTileLayer(theme: MapTheme): L.TileLayer {
  return L.tileLayer(theme.tileUrl, {
    attribution: theme.attribution,
    maxZoom: theme.maxZoom ?? 19,
    subdomains: theme.subdomains
  });
}
