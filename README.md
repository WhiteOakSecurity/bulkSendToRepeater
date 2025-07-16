# bulkSendToRepeater

The bulkSendToRepeater extension adds context menus to Burp Suite's request viewers, allowing requests to be forwarded to the Repeater tool in bulk.

* Requests may be added using their method and URI as Repeater Tab title (e.g., GET /api/v1/some/endpoint/1), limited to 70 characters, or
* Requests may be added using standard numbering for the Repeater Tab title

Version 1.0.2 adds support for the X-Request-Name header. If this request header is included when a request is forwarded to Repeater using the extension, the X-Request-Name value will be prepended in the tab name. The following script can be included in the Postman folder on pre-execution to include the header automnatically:

```
// pm.info.requestName gives you the name of the current request
const reqName = pm.info.requestName;

// Add (or overwrite) a header called X-Request-Name
pm.request.headers.upsert({
  key: 'X-Request-Name',
  value: reqName
});

```

![image](https://github.com/WhiteOakSecurity/bulkSendToRepeater/assets/10437631/a3d09bab-b49b-4710-b163-f1db0fe81250)
