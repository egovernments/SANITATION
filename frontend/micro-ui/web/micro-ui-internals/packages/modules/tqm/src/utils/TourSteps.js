export const Tour = {
  updateTest:[
    // {
    //   content:"Let me walk you through on how to upload test results", 
    //   target:".app-container",
    //   disableBeacon:true,
    //   placement:"center",
    //   title:"This is your Home Screen"
    // },
    {
      content:"Let me walk you through on how to upload test results.This is a list of tests that are pending to be updated. All the pending tests will end up in your inbox", 
      target:".notification",
      disableBeacon:true,
      placement:"top",
      title:"How to Upload Test Results"
    },
  ],
  viewTest:[
    {
      content:"Let me walk you through on how to search tests", 
      target:".app-container",
      disableBeacon:true,
      placement:"center",
      title:"This is your Home Screen"
    },
    {
      content:"From the Landing screen, click on the Treatment Quality Card. Click on next to move further", 
      target:".tiles-card-2",
      disableBeacon:true,
      placement:"auto",
      redirectTo:"/sanitation-ui/employee/tqm/home"
      // title:"This is your Home Screen"
    },
    {
      content:"This is your Home Screen for TQM", 
      target:".tqm-home-container",
      disableBeacon:true,
      placement:"center",
      hideBackButton:true
      // title:"This is your Home Screen"
    },
    {
      content:"From the TQM Card on Home Screen, click on the View Past Tests Link. Click on next to move further", 
      target:".complaint-links-container",
      disableBeacon:true,
      placement:"auto",
      redirectTo:"/sanitation-ui/employee/tqm/search-test-results"
      // title:"This is your Home Screen"
    },
    {
      content:"This is View Past Tests Screen. Here you will get the list of tests that are submitted, according to the filters that you have selected", 
      target:".ground-container",
      disableBeacon:true,
      placement:"center",
      hideBackButton:true
      // redirectTo:"/sanitation-ui/employee/tqm/search-test-results"
      // title:"This is your Home Screen"
    },
    {
      content:"You can search with applicable filters and sort the results using Filter and Sort Action respectively", 
      target:".searchBox",
      disableBeacon:true,
      placement:"bottom",
      // redirectTo:"/sanitation-ui/employee/tqm/search-test-results"
      // title:"This is your Home Screen"
    },
    //Add more steps corresponding to search results when search api is working fine

    
  ],
  viewDashboard:[

  ],
  viewSensors:[

  ]

}