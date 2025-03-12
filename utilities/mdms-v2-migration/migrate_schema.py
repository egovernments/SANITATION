import json
from urllib.parse import urljoin
from pathlib import Path

import utils
from payload import schemaCreate

URL = utils.get_env()["host"]
LOCAL_URL = utils.get_env()["localhost"]
ENDPOINTS = utils.get_api()["mdmsv2"]
REQINFO = utils.get_reqInfo()

def create_schema(schema_path, tenantId, is_portforward = True):

    base_url = LOCAL_URL if is_portforward else URL
    
    base_url = base_url + ENDPOINTS["schemaCreate"]

    with open(schema_path) as f:
        schema_data = json.load(f)

    for schema in schema_data:
        
        schema["tenantId"] = tenantId

        body = schemaCreate.SchemaCreate(RequestInfo=REQINFO, 
                                SchemaDefinition=schema).model_dump(by_alias=True)

        response = utils.make_request(method="POST",
                        url=base_url,
                        payload=body)

        utils.log_response(response)

        print(response.request.body, type(response.request.body))
        print(base_url, "\n", schema["code"], "\n")

def create_all_schema(schema_folder, tenantId, is_portforward = True):

    for item in Path(schema_folder).iterdir():
        if item.is_file():
            create_schema(item, tenantId, is_portforward)

# create_schema("schema/pqm.json", tenantId="as")
# create_all_schema("schema", tenantId="as")