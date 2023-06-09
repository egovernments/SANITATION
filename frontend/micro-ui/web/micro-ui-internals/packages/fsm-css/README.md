<!-- TODO: update this -->

# digit-ui-fsm-css

## Install

```bash
npm install --save @egovernments/digit-ui-fsm-css
```

## Limitation

```bash
This Package is more specific to DIGIT-UI's can be used across mission's
It is the base css for Sanitation UI
Parent CSS would be digit-ui-css(https://www.npmjs.com/package/@egovernments/digit-ui-css)
```

## Usage

After adding the dependency make sure you have this dependency in

```bash
frontend/micro-ui/web/package.json
```

```json
"@egovernments/digit-ui-fsm-css":"^0.0.11",
```

then navigate to App.js

```bash
frontend/micro-ui/web/public/index.html
```

```jsx
/** add this import **/

  <link rel="stylesheet" href="https://unpkg.com/@egovernments/digit-ui-fsm-css@0.0.11/dist/index.css" />

```

# Changelog

```bash
0.0.11 Fixed navbar header fsm citizen
0.0.10 Updated radio button,checkbox,label,card,header,erorr field for all fsm citizen
0.0.9 Rating issue fixed for fsm
0.0.8 homescreen card issue fixed for fsm
0.0.7 added the readme file
0.0.6 base version
```

## Published from DIGIT Sanitation

Digit Dev Repo (<https://github.com/egovernments/SANITATION>)

## License

MIT © [jagankumar-egov](https://github.com/jagankumar-egov)
