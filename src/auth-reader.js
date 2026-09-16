import fs from 'node:fs';
import { authCandidates } from './config.js';

// 读取本机当前登录态文件，提取关键信息。失败返回 null。
export function readLocalAuth() {
  for (const p of authCandidates()) {
    try {
      if (!fs.existsSync(p)) continue;
      const raw = fs.readFileSync(p, 'utf8');
      const o = JSON.parse(raw);
      const acc = o.account || o.accounts?.[0] || o.allAccounts?.[0] || {};
      const auth = o.auth || {};
      if (!auth.accessToken) continue;
      return {
        source: p,
        uid: acc.uid || null,
        nickname: acc.nickname || null,
        uin: acc.uin || null,
        phone: acc.phoneNumber || null,
        accountType: acc.type || 'personal',
        token: auth.accessToken,
        tokenType: auth.tokenType || 'Bearer',
        expiresAt: auth.expiresAt || null,
        refreshToken: auth.refreshToken || null,
      };
    } catch {
      // try next candidate
    }
  }
  return null;
}

// 未知到期时间不等于已过期；实际 API 失败会单独显示。
export function isExpired(expiresAt) {
  if (!expiresAt) return false;
  const ms = Number(expiresAt);
  if (!Number.isFinite(ms)) return false;
  return ms < Date.now();
}
