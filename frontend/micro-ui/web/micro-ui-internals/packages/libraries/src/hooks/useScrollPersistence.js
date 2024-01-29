/*
useScrollPersistence Hook
  This custom React Hook, useScrollPersistence, is designed to store and retrieve scroll positions based on the current URL. It utilizes the browser's localStorage to persistently store the scroll position when navigating between pages. Additionally, it handles scroll position restoration after a page reload.
  useScrollPersistence Hook

  This custom React Hook, useScrollPersistence, is designed to manage and persist the scroll position of a page based on its URL. It includes functionality to store the scroll position in the browser's localStorage and restore it when the user navigates between pages or refreshes the page.

  How it Works:

  - The hook uses the current URL as a key to store and retrieve scroll positions in localStorage.
  - On component mount, it retrieves the stored scroll position and sets it as the initial state.
  - Listens for scroll events and updates the scroll position state accordingly.
  - Utilizes localStorage to prevent unintended function calls during page reloads.
  - Handles scroll position restoration after a page reload.

  Methods:
  - `getUrlKey`: Generates a key based on the current URL for storing scroll positions.
  - `initialScrollPosition`: Retrieves the initial scroll position from localStorage.
  - `handleScroll`: Listens for scroll events and updates the scroll position state and localStorage.
  - `handleBeforeUnload`: Stores scroll position before a page unload to ensure proper restoration.
  
  How to Use:
  1. Call the hook in your functional component:
      const scrollPosition = Digit.Hooks.useScrollPersistence();

  2. The hook returns the current scroll position, which you can use in your component.

  3. To scroll to the persisted position when scroll persistence is enabled, use the following useEffect in the desired file:
      useEffect(() => {
      window.scrollTo(0, scrollPosition);
      }, [scrollPosition]);
  
  4. To destroy the stored scroll value, use the following use effect in the desired file where you want to remove:
      useEffect(() =>{
      localStorage.removeItem(URL_KEY);
      }, [])
  
  NOTE: 
    In mobile view, clicking on back will re run scroll function causing reset the scroll value to 0. So add to the "scrollValue" to enable a prevented check:  
      useEffect(() => {
        const checkValue = localStorage.getItem(URL_KEY);
        localStorage.setItem("scrollValue", checkValue);
      }, []); 
*/

import { useEffect, useState } from "react";
import throttle from "lodash/throttle";

const useScrollPersistence = () => {
  const getUrlKey = () => {
    // Use the current URL as the key
    return window.location.pathname;
  };

  const initialScrollPosition = () => {
    // Set the initial scroll position from storage
    const storedScrollPosition = localStorage.getItem(getUrlKey());
    return storedScrollPosition ? parseInt(storedScrollPosition, 10) : 0;
  };

  const [scrollPosition, setScrollPosition] = useState(initialScrollPosition());

  useEffect(() => {

    const handleScroll = () => {
      // functional calls when scrolling
      const checkValue = localStorage.getItem("scrollValue");
      const checkReloadValue = localStorage.getItem("reloadScrollValue");
      const isScrolling = localStorage.getItem("isScrolling");
      if (checkValue > 0) {
        localStorage.removeItem("scrollValue");
        return;
      }
      // Check to prevent unintended function calls
      if (checkReloadValue > 0 && isScrolling) {
        localStorage.removeItem("reloadScrollValue");
        return;
      }
      localStorage.removeItem("isScrolling");
      if (!isScrolling) {
        const currentScrollPosition = window.scrollY;
        setScrollPosition(currentScrollPosition);
        localStorage.setItem(getUrlKey(), window.scrollY);
      }
    };

    const handleBeforeUnload = () => {
      localStorage.setItem("reloadScrollValue", window.scrollY);
      localStorage.setItem("isScrolling", true);
      localStorage.setItem(getUrlKey(), window.scrollY);
    };

    // adding listener
    window.addEventListener("scroll", handleScroll);
    window.addEventListener("beforeunload", handleBeforeUnload);

    // Retrieve the stored scroll position on component mount
    const storedScrollPosition = localStorage.getItem(getUrlKey());
    if (storedScrollPosition) {
      setScrollPosition(parseInt(storedScrollPosition, 10));
    }

    return () => {
      // unmounting listener
      window.removeEventListener("scroll", handleScroll);
      window.removeEventListener("beforeunload", handleBeforeUnload);
    };
  }, []);

  return scrollPosition;
};

export default useScrollPersistence;
