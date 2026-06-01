import { useState } from 'react';
import { useNavigate } from 'react-router';
import { Package, Globe, Warehouse, Mail, Lock, User as UserIcon, LogIn, UserPlus } from 'lucide-react';

export function Portal() {
  const [isLogin, setIsLogin] = useState(true);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [name, setName] = useState('');
  const [role, setRole] = useState('/sales');
  const navigate = useNavigate();

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // Simulate auth
    navigate(role);
  };

  return (
    <div className="min-h-full flex items-center justify-center bg-slate-50 py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-md w-full space-y-8 bg-white p-8 rounded-2xl shadow-xl border border-[var(--border)]">
        
        {/* Header */}
        <div className="text-center">
          <div className="mx-auto w-16 h-16 bg-blue-100 rounded-2xl flex items-center justify-center mb-6 shadow-sm">
            <Globe className="w-8 h-8 text-blue-600" />
          </div>
          <h2 className="text-3xl font-bold text-[var(--text-primary)] tracking-tight">
            {isLogin ? 'Đăng nhập hệ thống' : 'Đăng ký tài khoản'}
          </h2>
          <p className="mt-2 text-sm text-[var(--text-secondary)]">
            Hệ thống Đặt hàng Nhập khẩu
          </p>
        </div>

        {/* Auth Form */}
        <form className="mt-8 space-y-6" onSubmit={handleSubmit}>
          <div className="space-y-4">
            {/* Name Field (Only for Signup) */}
            {!isLogin && (
              <div>
                <label className="block text-sm font-medium text-[var(--text-primary)] mb-1">
                  Họ và tên
                </label>
                <div className="relative">
                  <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                    <UserIcon className="h-5 w-5 text-gray-400" />
                  </div>
                  <input
                    type="text"
                    required
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    className="block w-full pl-10 pr-3 py-2 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 sm:text-sm transition-colors"
                    placeholder="Nguyễn Văn A"
                  />
                </div>
              </div>
            )}

            {/* Email Field */}
            <div>
              <label className="block text-sm font-medium text-[var(--text-primary)] mb-1">
                Email
              </label>
              <div className="relative">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                  <Mail className="h-5 w-5 text-gray-400" />
                </div>
                <input
                  type="email"
                  required
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  className="block w-full pl-10 pr-3 py-2 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 sm:text-sm transition-colors"
                  placeholder="email@example.com"
                />
              </div>
            </div>

            {/* Password Field */}
            <div>
              <label className="block text-sm font-medium text-[var(--text-primary)] mb-1">
                Mật khẩu
              </label>
              <div className="relative">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                  <Lock className="h-5 w-5 text-gray-400" />
                </div>
                <input
                  type="password"
                  required
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  className="block w-full pl-10 pr-3 py-2 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 sm:text-sm transition-colors"
                  placeholder="••••••••"
                />
              </div>
            </div>

            {/* Role Selection */}
            <div>
              <label className="block text-sm font-medium text-[var(--text-primary)] mb-1">
                Vai trò hệ thống
              </label>
              <div className="grid grid-cols-1 gap-3 mt-2">
                <label
                  className={`relative flex items-center p-4 cursor-pointer rounded-xl border-2 transition-all ${
                    role === '/sales'
                      ? 'border-blue-500 bg-blue-50'
                      : 'border-gray-200 hover:border-blue-200'
                  }`}
                >
                  <input
                    type="radio"
                    name="role"
                    value="/sales"
                    className="sr-only"
                    checked={role === '/sales'}
                    onChange={(e) => setRole(e.target.value)}
                  />
                  <Package className={`w-6 h-6 mr-3 ${role === '/sales' ? 'text-blue-600' : 'text-gray-400'}`} />
                  <div>
                    <span className={`block text-sm font-medium ${role === '/sales' ? 'text-blue-900' : 'text-gray-900'}`}>
                      Bộ phận Bán hàng
                    </span>
                  </div>
                </label>

                <label
                  className={`relative flex items-center p-4 cursor-pointer rounded-xl border-2 transition-all ${
                    role === '/overseas'
                      ? 'border-indigo-500 bg-indigo-50'
                      : 'border-gray-200 hover:border-indigo-200'
                  }`}
                >
                  <input
                    type="radio"
                    name="role"
                    value="/overseas"
                    className="sr-only"
                    checked={role === '/overseas'}
                    onChange={(e) => setRole(e.target.value)}
                  />
                  <Globe className={`w-6 h-6 mr-3 ${role === '/overseas' ? 'text-indigo-600' : 'text-gray-400'}`} />
                  <div>
                    <span className={`block text-sm font-medium ${role === '/overseas' ? 'text-indigo-900' : 'text-gray-900'}`}>
                      Bộ phận Đặt hàng Quốc tế
                    </span>
                  </div>
                </label>

                <label
                  className={`relative flex items-center p-4 cursor-pointer rounded-xl border-2 transition-all ${
                    role === '/warehouse'
                      ? 'border-emerald-500 bg-emerald-50'
                      : 'border-gray-200 hover:border-emerald-200'
                  }`}
                >
                  <input
                    type="radio"
                    name="role"
                    value="/warehouse"
                    className="sr-only"
                    checked={role === '/warehouse'}
                    onChange={(e) => setRole(e.target.value)}
                  />
                  <Warehouse className={`w-6 h-6 mr-3 ${role === '/warehouse' ? 'text-emerald-600' : 'text-gray-400'}`} />
                  <div>
                    <span className={`block text-sm font-medium ${role === '/warehouse' ? 'text-emerald-900' : 'text-gray-900'}`}>
                      Bộ phận Quản lý Kho
                    </span>
                  </div>
                </label>
              </div>
            </div>
          </div>

          {/* Submit Button */}
          <div>
            <button
              type="submit"
              className="group relative w-full flex justify-center py-3 px-4 border border-transparent text-sm font-medium rounded-lg text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition-colors shadow-md"
            >
              <span className="absolute left-0 inset-y-0 flex items-center pl-3">
                {isLogin ? (
                  <LogIn className="h-5 w-5 text-blue-500 group-hover:text-blue-400 transition-colors" />
                ) : (
                  <UserPlus className="h-5 w-5 text-blue-500 group-hover:text-blue-400 transition-colors" />
                )}
              </span>
              {isLogin ? 'Đăng nhập' : 'Đăng ký tài khoản'}
            </button>
          </div>

          {/* Toggle Mode */}
          <div className="text-center mt-4">
            <button
              type="button"
              onClick={() => setIsLogin(!isLogin)}
              className="text-sm text-blue-600 hover:text-blue-500 font-medium"
            >
              {isLogin
                ? 'Chưa có tài khoản? Đăng ký ngay'
                : 'Đã có tài khoản? Đăng nhập'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}