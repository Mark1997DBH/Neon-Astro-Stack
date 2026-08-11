// 本地存储封装：支持 localStorage / sessionStorage，自动 JSON 序列化

type StorageType = 'local' | 'session'

class StorageHelper {
  private storage: Storage

  constructor(type: StorageType = 'local') {
    this.storage = type === 'local' ? localStorage : sessionStorage
  }

  get<T>(key: string): T | null {
    const raw = this.storage.getItem(key)
    if (raw === null) return null
    try {
      return JSON.parse(raw) as T
    } catch {
      // 非 JSON 字符串直接返回
      return raw as unknown as T
    }
  }

  set<T>(key: string, value: T): void {
    const text = typeof value === 'string' ? value : JSON.stringify(value)
    this.storage.setItem(key, text)
  }

  remove(key: string): void {
    this.storage.removeItem(key)
  }

  removeAll(): void {
    this.storage.clear()
  }

  has(key: string): boolean {
    return this.storage.getItem(key) !== null
  }
}

export const localStore = new StorageHelper('local')
export const sessionStore = new StorageHelper('session')

export default localStore
