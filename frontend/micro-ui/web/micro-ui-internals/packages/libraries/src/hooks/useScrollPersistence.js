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
