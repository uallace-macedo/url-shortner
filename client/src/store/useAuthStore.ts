import { create } from 'zustand';
import { persist } from 'zustand/middleware';
import { apiClient } from '../api/client';

interface User {
  id: string;
  name: string;
  email: string;
  createdAt: string;
}

interface AuthState {
  user: User | null;
  isAuthenticated: boolean;
  login: (data: any) => Promise<void>;
  register: (data: any) => Promise<void>;
  logout: () => Promise<void>;
  checkAuth: () => Promise<void>;
}

export const useAuthStore = create<AuthState>()(
  persist(
    (set) => ({
      user: null,
      isAuthenticated: false,

      login: async (credentials) => {
        const user = await apiClient.post<User>('/users/login', credentials);
        set({ user, isAuthenticated: true });
      },

      register: async (credentials) => {
        const user = await apiClient.post<User>('/users/register', credentials);
        set({ user, isAuthenticated: true });
      },

      logout: async () => {
        try {
          await apiClient.post('/users/logout');
        } catch (e) {
        }
        set({ user: null, isAuthenticated: false });
      },

      checkAuth: async () => {
        try {
          const user = await apiClient.get<User>('/users/me');
          set({ user, isAuthenticated: true });
        } catch (e) {
          set({ user: null, isAuthenticated: false });
        }
      }
    }),
    {
      name: 'auth-storage',
    }
  )
);
