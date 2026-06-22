const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080';

async function request<T>(path: string, init?: RequestInit): Promise<T> {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    headers: {
      'Content-Type': 'application/json',
      ...(init?.headers ?? {}),
    },
    ...init,
  });

  if (!response.ok) {
    throw new Error(`Request failed with status ${response.status}`);
  }

  return response.json() as Promise<T>;
}

export const api = {
  login(email: string, password: string) {
    return request<{ token: string }>('/api/auth/login', {
      method: 'POST',
      body: JSON.stringify({ email, password }),
    });
  },
  register(email: string, password: string) {
    return request<{ token: string }>('/api/auth/register', {
      method: 'POST',
      body: JSON.stringify({ email, password }),
    });
  },
  dashboard(token: string) {
    return request<{ balance: number }>('/api/dashboard', {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
  },
  account(token: string) {
    return request<{ id: string; ownerEmail: string; balance: number }>('/api/accounts/me', {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
  },
  deposit(token: string, amount: number, description: string) {
    return request<{ id: string; ownerEmail: string; balance: number }>('/api/accounts/deposit', {
      method: 'POST',
      headers: {
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({ amount, description }),
    });
  },
  transfer(token: string, toEmail: string, amount: number, description: string) {
    return request<void>('/api/accounts/transfer', {
      method: 'POST',
      headers: {
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({ toEmail, amount, description }),
    });
  },
  pix(token: string, toEmail: string, amount: number, description: string) {
    return request<void>('/api/accounts/pix', {
      method: 'POST',
      headers: {
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({ toEmail, amount, description }),
    });
  },
  statements(token: string) {
    return request<Array<{ id: string; accountEmail: string; type: string; amount: number; description: string; createdAt: string }>>('/api/statements', {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
  },
};
