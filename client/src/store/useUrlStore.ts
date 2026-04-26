import { create } from 'zustand';
import { apiClient } from '../api/client';

export interface UrlItem {
  id: string;
  originalUrl: string;
  customSlug: string;
  createdAt: string;
  ttlDays: number | null;
  totalClicks: number;
  maxCount: number | null;
  active: boolean;
}

export interface UrlStats {
  totalClicks: number;
  clicksPerDay: { date: string; count: number }[];
  topReferrers: { referrer: string; count: number }[];
}

interface PageResponse<T> {
  content: T[];
  pageable: any;
  last: boolean;
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
  first: boolean;
  numberOfElements: number;
  empty: boolean;
}

interface UrlState {
  urls: UrlItem[];
  page: number;
  hasMore: boolean;
  isLoading: boolean;
  fetchUrls: (reset?: boolean) => Promise<void>;
  createUrl: (data: { url: string; custom_slug?: string; expires_at?: string; max_count?: number }) => Promise<void>;
  updateUrl: (id: string, data: { new_slug?: string; max_click_count?: number; active?: boolean }) => Promise<void>;
  deleteUrl: (id: string) => Promise<void>;
  clearStore: () => void;
}

export const useUrlStore = create<UrlState>((set, get) => ({
  urls: [],
  page: 0,
  hasMore: true,
  isLoading: false,

  fetchUrls: async (reset = false) => {
    const { page, hasMore, isLoading, urls } = get();

    if (isLoading) return;
    if (!reset && !hasMore) return;

    const currentPage = reset ? 0 : page;
    set({ isLoading: true });

    try {
      const response = await apiClient.get<PageResponse<UrlItem>>(`/urls?page=${currentPage}&size=10`);

      set({
        urls: reset ? response.content : [...urls, ...response.content],
        page: currentPage + 1,
        hasMore: !response.last,
        isLoading: false
      });
    } catch (error) {
      set({ isLoading: false });
      throw error;
    }
  },

  createUrl: async (data) => {
    const newUrl = await apiClient.post<UrlItem>('/urls', data);
    set((state) => ({
      urls: [newUrl, ...state.urls]
    }));
  },

  updateUrl: async (id, data) => {
    const updatedUrl = await apiClient.patch<UrlItem>(`/urls/${id}`, data);
    set((state) => ({
      urls: state.urls.map(url => {
        if (url.id === id) {
          if (updatedUrl && updatedUrl.id) {
            return updatedUrl;
          }
          return {
            ...url,
            ...(data.new_slug !== undefined && { customSlug: data.new_slug }),
            ...(data.max_click_count !== undefined && { maxCount: data.max_click_count }),
            ...(data.active !== undefined && { active: data.active })
          };
        }
        return url;
      })
    }));
  },

  deleteUrl: async (id) => {
    await apiClient.delete(`/urls/${id}`);
    set((state) => ({
      urls: state.urls.filter(url => url.id !== id)
    }));
  },

  clearStore: () => {
    set({ urls: [], page: 0, hasMore: true, isLoading: false });
  }
}));
