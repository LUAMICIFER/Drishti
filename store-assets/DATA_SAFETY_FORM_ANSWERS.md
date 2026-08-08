# Play Console — Data Safety Form Answers

Copy these into Play Console → App content → Data safety.

## Does your app collect or share any required user data types?
**Yes.**

## Data types collected

### Personal info
- **Name** — collected (account profile). Not shared with 3rd parties (except Firebase, which is your data processor, not a "3rd party" for this form — Play Console has a separate checkbox for service providers, mark Firebase there).
- **Email address** — collected (sign in / account). Used for: Account management, App functionality.
- **Phone number** — collected if user provides it. Used for: Account management.

### Financial info
- **Purchase history** — collected (which courses purchased). Used for: App functionality.
- Payment card/UPI details — **NOT collected by your app** — handled entirely by Razorpay's SDK, which has its own data handling. You can answer "No" for card details specifically, since your app code never touches raw card data.

### App activity
- **App interactions** — collected (course views, video watch progress). Used for: App functionality, Analytics.

### Device or other IDs
- **Device ID** — collected (Android ID via `DeviceCheckHelper`, used for multi-device login detection). Used for: Fraud prevention, security.

## Is all of the user data collected encrypted in transit?
**Yes** (Firebase and Razorpay both use HTTPS/TLS).

## Do you provide a way for users to request that their data be deleted?
**Yes** — mark this, then in your Privacy Policy make sure there's a way to request deletion (the draft I wrote includes your contact email for this — you handle deletion requests manually by deleting their Firestore user doc + Firebase Auth account).

## Data collection purposes — for each data type above, select:
- App functionality
- Account management
- (Do NOT select "Advertising or marketing" unless you actually run ads — you don't appear to.)

## Is data collection required or optional?
- Name, email: **Required** (needed to create account)
- Phone: mark as **Optional** if signup doesn't force it, else Required
- Device ID: **Required** (used for security, not user-optional)

---

**Note:** This is drafted from reading your code (Firebase Auth, Firestore, Razorpay, DeviceCheckHelper). Double check against what's actually collected before submitting — Play Console can suspend apps for inaccurate Data Safety answers, so don't just copy-paste blindly if your data collection changes later.
