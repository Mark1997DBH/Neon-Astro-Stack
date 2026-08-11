import type { Config } from 'tailwindcss'

// TailwindCSS 配置：霓虹辉光暗色主题
export default {
  content: ['./index.html', './src/**/*.{vue,ts,tsx,js,jsx}'],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        neon: {
          cyan: '#22d3ee',
          purple: '#a855f7',
          pink: '#ec4899',
          bg: '#0a0a1a'
        }
      },
      boxShadow: {
        neon: '0 0 20px rgba(168,85,247,.5)',
        'neon-cyan': '0 0 20px rgba(34,211,238,.5)',
        'neon-pink': '0 0 20px rgba(236,72,153,.5)'
      },
      fontFamily: {
        sans: ['Orbitron', 'Inter', 'system-ui', 'sans-serif']
      },
      keyframes: {
        glow: {
          '0%, 100%': { boxShadow: '0 0 5px rgba(168,85,247,.5)' },
          '50%': { boxShadow: '0 0 25px rgba(168,85,247,.9)' }
        },
        'pulse-slow': {
          '0%, 100%': { opacity: '1' },
          '50%': { opacity: '0.6' }
        },
        float: {
          '0%, 100%': { transform: 'translateY(0)' },
          '50%': { transform: 'translateY(-10px)' }
        }
      },
      animation: {
        glow: 'glow 2s ease-in-out infinite',
        'pulse-slow': 'pulse-slow 3s ease-in-out infinite',
        float: 'float 4s ease-in-out infinite'
      },
      backgroundImage: {
        'cosmic': 'radial-gradient(circle at 20% 30%, rgba(168,85,247,0.15), transparent 40%), radial-gradient(circle at 80% 70%, rgba(34,211,238,0.15), transparent 40%)'
      }
    }
  },
  plugins: []
} satisfies Config
