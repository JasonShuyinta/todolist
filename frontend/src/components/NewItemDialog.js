import React, { useState, useContext } from "react";
import {
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  Button,
} from "@mui/material";
import ReactQuill from "react-quill";
import "react-quill/dist/quill.snow.css";
import axios from "axios";
import { fullScreenFormats, fullScreenModules, SERVER_URL } from "./Constants";
import { Context } from "../context/Context";

export default function NewItemDialog({ open, handleClose }) {
  const { config, refresh, setRefresh } = useContext(Context);

  const [text, setText] = useState("");

  const handleCreateNewItem = () => {
    axios
      .post(
        `${SERVER_URL}/item/saveItem`,
        {
          text,
        },
        config
      )
      .then((res) => {
        if (res.status === 200) {
          setRefresh(!refresh);
          handleClose();
          setText("");
        }
      })
      .catch((err) => alert("Something went wrong, please try again!"));
  };

  return (
    <Dialog open={open} onClose={handleClose} fullWidth>
      <DialogTitle>New item</DialogTitle>
      <DialogContent>
        <DialogContentText>Create new item</DialogContentText>
        <ReactQuill
          style={{ marginTop: "1rem" }}
          value={text}
          modules={fullScreenModules}
          formats={fullScreenFormats}
          onChange={setText}
          placeholder={"Write here your text"}
        />
      </DialogContent>
      <DialogActions>
        <Button onClick={handleClose} color="secondary" variant="contained">
          Cancel
        </Button>
        <Button
          onClick={handleCreateNewItem}
          color="primary"
          variant="contained"
        >
          Create
        </Button>
      </DialogActions>
    </Dialog>
  );
}
