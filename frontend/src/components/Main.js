import React, { useContext, useEffect, useState } from "react";
import { Grid, IconButton, Paper, Typography } from "@mui/material";
import axios from "axios";
import AddCircleRoundedIcon from "@mui/icons-material/AddCircleRounded";
import { Context } from "../context/Context";
import NewItemDialog from "./NewItemDialog";
import { SERVER_URL } from "./Constants";
import ItemList from "./ItemList";

export default function Main() {
  const context = useContext(Context);

  const { config, user, refresh, darkTheme } = context;
  const [todoItems, setTodoItems] = useState([]);
  const [inProgressItems, setInProgressItems] = useState([]);
  const [doneItems, setDoneItems] = useState([]);
  const [openNewItemDialog, setOpenNewItemDialog] = useState(false);

  useEffect(() => {
    if (config) {
      axios
        .get(`${SERVER_URL}/item/getUsersItem`, config)
        .then((res) => {
          setTodoItems(res.data.filter((el) => el.state === "TODO"));
          setInProgressItems(
            res.data.filter((el) => el.state === "IN_PROGRESS")
          );
          setDoneItems(res.data.filter((el) => el.state === "DONE"));
        })
        .catch((err) => { 
          console.log(err); });
    } else {
      setTodoItems([]);
      setDoneItems([]);
      setInProgressItems([]);
    }
  }, [user, config, refresh]);

  const handleNewItem = () => {
    if(user) {
      setOpenNewItemDialog(true);
    } else {
      alert("You need to login first!")
    }
  };

  const handleCloseNewItemDialog = () => {
    setOpenNewItemDialog(false);
  };

  return (
    <div className={`main-container ${darkTheme && "dark-body-theme"} `}>
      <div className="p-1">
        <Grid container spacing={3}>
          <Grid item xs={12} md={4}>
            <Paper
              elevation={3}
              className={`paper-container p-1 ${
                darkTheme
                  ? "dark-theme-paper-container"
                  : "light-theme-paper-container"
              }`}
            >
              <div className="center-and-align mb-1 ">
                <Typography variant="h5">To do</Typography>
              </div>
              <div className="center-and-align mb-1">
                <IconButton onClick={handleNewItem}>
                  <AddCircleRoundedIcon
                    fontSize="large"
                    style={{ color: darkTheme ? "white" : "black" }}
                  />
                </IconButton>
              </div>
              <ItemList items={todoItems} />
            </Paper>
          </Grid>
          <Grid item xs={12} md={4}>
            <Paper
              elevation={3}
              className={`paper-container p-1 ${
                darkTheme
                  ? "dark-theme-paper-container"
                  : "light-theme-paper-container"
              }`}
            >
              <div className="center-and-align mb-1">
                <Typography variant="h5">In progress</Typography>
              </div>
              <ItemList items={inProgressItems} />
            </Paper>
          </Grid>
          <Grid item xs={12} md={4}>
            <Paper
              elevation={3}
              className={`paper-container p-1 ${
                darkTheme
                  ? "dark-theme-paper-container"
                  : "light-theme-paper-container"
              }`}
            >
              <div className="center-and-align mb-1">
                <Typography variant="h5">Done</Typography>
              </div>
              <ItemList items={doneItems} />
            </Paper>
          </Grid>
        </Grid>
      </div>
      <NewItemDialog
        open={openNewItemDialog}
        handleClose={handleCloseNewItemDialog}
      />
    </div>
  );
}
