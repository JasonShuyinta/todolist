import React, { useState, useEffect, createContext } from "react";
import axios from "axios";
import { SERVER_URL } from "../components/Constants";
export const Context = createContext();

export const ContextProvider = (props) => {
  const [darkTheme, setDarkTheme] = useState(false);
  const [user, setUser] = useState("");
  const [refresh, setRefresh] = useState(false);
  const [config, setConfig] = useState(
    localStorage.getItem("todo-list-access-token")
      ? {
          headers: {
            authorization: `Bearer ${localStorage.getItem(
              "todo-list-access-token"
            )}`,
          },
        }
      : null
  );

  useEffect(() => {
    if (config) {
      axios
        .get(`${SERVER_URL}/auth/getUserFromToken`, config)
        .then((res) => setUser(res.data))
        .catch((err) => {
          console.log(err);
        });
    } else setUser("");
  }, [config]);

  return (
    <Context.Provider
      value={{
        user,
        setUser,
        config,
        setConfig,
        refresh,
        setRefresh,
        darkTheme,
        setDarkTheme,
      }}
    >
      {props.children}
    </Context.Provider>
  );
};
