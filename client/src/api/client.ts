export const API_BASE_URL = 'http://localhost:4000/api/v1';
export const REDIRECTOR_BASE_URL = 'http://localhost:4001';

export interface ApiError {
  timestamp: string;
  errors: string[];
  details: string;
}

export class ApiException extends Error {
  public errors: string[];
  public details: string;

  constructor(errorData: ApiError) {
    super(errorData.errors.join(', '));
    this.errors = errorData.errors;
    this.details = errorData.details;
  }
}

function getXsrfToken(): string | null {
  const match = document.cookie.match(/(?:^|;\s*)XSRF-TOKEN=([^;]*)/);
  return match ? decodeURIComponent(match[1]) : null;
}

async function request<T>(endpoint: string, options: RequestInit = {}): Promise<T> {
  const url = `${API_BASE_URL}${endpoint}`;

  const headers = new Headers(options.headers || {});

  if (!(options.body instanceof FormData)) {
    headers.set('Content-Type', 'application/json');
  }

  const method = options.method?.toUpperCase() || 'GET';
  if (['POST', 'PATCH', 'DELETE', 'PUT'].includes(method)) {
    const token = getXsrfToken();
    if (token) {
      headers.set('X-XSRF-TOKEN', token);
    }
  }

  const response = await fetch(url, {
    ...options,
    headers,
    credentials: 'include',
  });

  if (!response.ok) {
    let errorData: ApiError;
    try {
      errorData = await response.json();
    } catch {
      errorData = {
        timestamp: new Date().toISOString(),
        errors: [response.statusText || 'An unexpected error occurred'],
        details: 'Unknown error',
      };
    }
    throw new ApiException(errorData);
  }

  if (response.status === 204) {
    return {} as T;
  }

  const data = await response.json();
  return data as T;
}

export const apiClient = {
  get: <T>(endpoint: string, options?: RequestInit) =>
    request<T>(endpoint, { ...options, method: 'GET' }),

  post: <T>(endpoint: string, body?: any, options?: RequestInit) =>
    request<T>(endpoint, { ...options, method: 'POST', body: body ? JSON.stringify(body) : undefined }),

  patch: <T>(endpoint: string, body?: any, options?: RequestInit) =>
    request<T>(endpoint, { ...options, method: 'PATCH', body: body ? JSON.stringify(body) : undefined }),

  delete: <T>(endpoint: string, options?: RequestInit) =>
    request<T>(endpoint, { ...options, method: 'DELETE' }),
};
