import dayjs from 'dayjs'
import 'dayjs/locale/zh-cn'
import relativeTime from 'dayjs/plugin/relativeTime'

dayjs.extend(relativeTime)
dayjs.locale('zh-cn')

// 日期格式化
export function formatDate(date: string | number | Date | undefined, pattern = 'YYYY-MM-DD'): string {
  if (!date) return '-'
  return dayjs(date).format(pattern)
}

// 日期时间格式化
export function formatDateTime(date: string | number | Date | undefined): string {
  if (!date) return '-'
  return dayjs(date).format('YYYY-MM-DD HH:mm:ss')
}

// 相对时间
export function fromNow(date: string | number | Date): string {
  return dayjs(date).fromNow()
}

// 根据出生日期返回星座（MM-DD）
export function getZodiacSign(birthDate: string | Date): string {
  const d = typeof birthDate === 'string' ? new Date(birthDate) : birthDate
  if (isNaN(d.getTime())) return 'Unknown'
  const month = d.getMonth() + 1
  const day = d.getDate()

  const ranges: Array<{ sign: string; from: [number, number]; to: [number, number] }> = [
    { sign: 'Capricorn', from: [12, 22], to: [1, 19] },
    { sign: 'Aquarius', from: [1, 20], to: [2, 18] },
    { sign: 'Pisces', from: [2, 19], to: [3, 20] },
    { sign: 'Aries', from: [3, 21], to: [4, 19] },
    { sign: 'Taurus', from: [4, 20], to: [5, 20] },
    { sign: 'Gemini', from: [5, 21], to: [6, 21] },
    { sign: 'Cancer', from: [6, 22], to: [7, 22] },
    { sign: 'Leo', from: [7, 23], to: [8, 22] },
    { sign: 'Virgo', from: [8, 23], to: [9, 22] },
    { sign: 'Libra', from: [9, 23], to: [10, 23] },
    { sign: 'Scorpio', from: [10, 24], to: [11, 22] },
    { sign: 'Sagittarius', from: [11, 23], to: [12, 21] }
  ]

  for (const r of ranges) {
    const [fm, fd] = r.from
    const [tm, td] = r.to
    if (fm === tm) {
      if (month === fm && day >= fd && day <= td) return r.sign
    } else if (month === fm && day >= fd) {
      return r.sign
    } else if (month === tm && day <= td) {
      return r.sign
    }
  }
  return 'Unknown'
}

// 星座中文映射
export const ZODIAC_CN: Record<string, string> = {
  Aries: '白羊座',
  Taurus: '金牛座',
  Gemini: '双子座',
  Cancer: '巨蟹座',
  Leo: '狮子座',
  Virgo: '处女座',
  Libra: '天秤座',
  Scorpio: '天蝎座',
  Sagittarius: '射手座',
  Capricorn: '摩羯座',
  Aquarius: '水瓶座',
  Pisces: '双鱼座',
  Unknown: '未知'
}

// 星座图标 emoji
export const ZODIAC_EMOJI: Record<string, string> = {
  Aries: '♈',
  Taurus: '♉',
  Gemini: '♊',
  Cancer: '♋',
  Leo: '♌',
  Virgo: '♍',
  Libra: '♎',
  Scorpio: '♏',
  Sagittarius: '♐',
  Capricorn: '♑',
  Aquarius: '♒',
  Pisces: '♓',
  Unknown: '✦'
}
