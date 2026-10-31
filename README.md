# Lock Player (LSPosed / Vector module)

Big-art media player on the lock screen. Hooks `com.android.systemui` only.

## Build
Push to GitHub -> Actions -> "Build module" -> download the `LockPlayer` artifact (APK).

## Install
1. Install the APK.
2. Vector manager -> Modules -> enable "Lock Player" -> scope: System UI.
3. Restart SystemUI: `su -c killall com.android.systemui` (or reboot).
4. Play something, lock the phone.

## Tweaks (top of LockPlayer.java / MainHook.java)
- `HIDE_STOCK_PLAYER` – hide the normal Android lock screen media card.
- `ART_MAX_WIDTH_FRACTION`, `ART_RADIUS_DP`, `GAP_DP`, `SIDE_PAD_DP` – art size / spacing.
- `CLOCK_FALLBACK_BOTTOM_FRACTION`, `ICON_FALLBACK_TOP_FRACTION` – used only if the clock /
  fingerprint view can't be found at runtime.

Layout: art sits between the clock and the fingerprint icon; title, seek bar and the
prev / play / next pills sit below the fingerprint icon. Hidden on AOD and screen-off.

## Debug
`logcat | grep -i lockplayer` and the Vector module log.
