const formatDate = (date) => {
  const day = date.getDate();
  const month = date.toLocaleString("default", { month: "short" });
  const year = date.getFullYear();
  return `${day}${ordinalSuffix(day)} ${month}, ${year}`;
};

const ordinalSuffix = (day) => {
  if (day >= 11 && day <= 13) {
    return "th";
  }
  const lastDigit = day % 10;
  switch (lastDigit) {
    case 1:
      return "st";
    case 2:
      return "nd";
    case 3:
      return "rd";
    default:
      return "th";
  }
};

const getDateRange = (startDate) => {
  const today = new Date();
  const lastMonthEnd = new Date(today.getFullYear(), today.getMonth(), 0);
  const lastMonthStart = new Date(today.getFullYear(), today.getMonth() - 1, 1);

  const formattedToday = formatDate(today);
  const formattedLastMonthEnd = formatDate(lastMonthEnd);
  const formattedStartDate = formatDate(startDate);

  return {
    today: formattedToday,
    lastMonthEnd: formattedLastMonthEnd,
    startDate: formattedStartDate,
  };
};

export default getDateRange;
