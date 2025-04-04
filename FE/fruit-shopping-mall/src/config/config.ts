interface AppConfig {
    apiUrl: string;
    apiTimeout: number;
    imagePath: string;
    defaultPageSize: number;
}
  
const devConfig: AppConfig = {
  apiUrl: 'http://localhost:8080/api',
  apiTimeout: 10000,
  imagePath: '/images',
  defaultPageSize: 10
};

const prodConfig: AppConfig = {
  apiUrl: 'https://api.fruitmall.com/api',
  apiTimeout: 15000,
  imagePath: '/images',
  defaultPageSize: 10
};

const config: AppConfig = process.env.NODE_ENV === 'production' ? prodConfig : devConfig;

export default config;