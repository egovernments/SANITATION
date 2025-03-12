import requests, json
from urllib.parse import urljoin
from pathlib import Path

import utils
from payload import dataCreate

URL = utils.get_env()["host"]
LOCAL_URL = utils.get_env()["localhost"]
ENDPOINTS = utils.get_api()["mdmsv2"]
REQINFO = utils.get_reqInfo()

def create_data(data_path, tenantId, is_portforward = True):

    base_url = LOCAL_URL if is_portforward else URL
    schemaCode = Path(data_path).stem    
    base_url = urljoin(base_url, ENDPOINTS["dataCreate"])

    with open(data_path) as f:
        data_array = json.load(f)

    for data in data_array:
    
        mdms_data = dataCreate.Mdms(tenantId = tenantId,
                                    schemaCode = schemaCode,
                                    data=data, isActive=True)

        body = dataCreate.DataCreate(RequestInfo=REQINFO, 
                                    Mdms=mdms_data).model_dump(by_alias=True)

        response = utils.make_request(method="POST",
                        url=base_url+schemaCode,
                        payload=body)

        utils.log_response(response)

        print(base_url+schemaCode, "\n", body, "\n", response.json())

def create_all_data(data_folder, tenantId, is_portforward = True):

    for item in Path(data_folder).iterdir():
        if item.is_file():
            create_data(item, tenantId, is_portforward)

# create_data("data/BillingService.BusinessService.json", tenantId="pg")
# create_all_data("data", tenantId="pg")