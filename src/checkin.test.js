import test from 'node:test';
import assert from 'node:assert/strict';
import { checkinAccount } from './engine.js';
import { isExpired } from './auth-reader.js';

test('unknown or near-future expiry does not disable an account', () => {
  assert.equal(isExpired(null), false);
  assert.equal(isExpired(Date.now() + 60000), false);
  assert.equal(isExpired(Date.now() - 60000), true);
});

test('check-in records only confirmed business success', async () => {
  const original = globalThis.fetch;
  try {
    for (const [http, body, success] of [
      [200, {code: 0}, true],
      [200, {code: 10001}, true],
      [200, {code: 500, msg: '凭证已过期'}, false],
      [401, {code: 10001}, false],
      [200, 'invalid response', false],
    ]) {
      globalThis.fetch = async () => new Response(
        typeof body === 'string' ? body : JSON.stringify(body), {status: http});
      const account = {token: 'synthetic', lastCheckin: null};
      const result = await checkinAccount(account);
      assert.equal(result.ok, success);
      assert.equal(account.lastCheckin !== null, success);
    }
  } finally {
    globalThis.fetch = original;
  }
});
