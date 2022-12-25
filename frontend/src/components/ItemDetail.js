import React, { useState, useContext, useEffect } from "react";
import PropTypes from "prop-types";
import {
  Typography,
  Dialog,
  DialogContent,
  IconButton,
  DialogTitle,
  Card,
  DialogActions,
  Button,
  Box
} from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import axios from "axios";
import { SERVER_URL } from "./Constants";
import { Context } from "../context/Context";
import parse from 'html-react-parser';
import { fullScreenFormats, fullScreenModules } from "./Constants";
import ReactQuill from "react-quill";

const BootstrapDialogTitle = (props) => {
  const { children, onClose, ...other } = props;

  return (
    <DialogTitle sx={{ m: 0, p: 2 }} {...other}>
      {children}
      {onClose ? (
        <IconButton
          aria-label="close"
          onClick={onClose}
          sx={{
            position: "absolute",
            right: 8,
            top: 8,
            color: (theme) => theme.palette.grey[500],
          }}
        >
          <CloseIcon />
        </IconButton>
      ) : null}
    </DialogTitle>
  );
};

BootstrapDialogTitle.propTypes = {
  children: PropTypes.node,
  onClose: PropTypes.func.isRequired,
};

export default function ItemDetail({
  item,
  openItemDetailDialog,
  handleClose,
}) {
  const [editText, setEditText] = useState(false);
  const [newText, setNewText] = useState("");
  const [openDelete, setOpenDelete] = useState(false);

  const context = useContext(Context);
  const { user, refresh, setRefresh } = context;

  const saveNewText = () => {
    axios
      .put(`${SERVER_URL}/item/editText`, {
        id: item.id,
        state: item.state,
        text: newText,
        user,
      })
      .then((res) => {
        if (res.status === 200) {
          setEditText(false);
          setRefresh(!refresh);
          handleClose();
        }
      })
      .catch((err) => alert("Something went wrong, please try again!"));
  };

  const handleDeleteItem = () => {
    setOpenDelete(true);
  };

  const handleDelete = (itemId) => {
    axios
      .delete(`${SERVER_URL}/item/${itemId}`)
      .then((res) => {
        if (res.status === 200) {
          setRefresh(!refresh);
          setOpenDelete(false);
          handleClose();
        }
      })
      .catch((err) => alert("Something went wrong, please try again!"));
  };

  useEffect(() => {
    setNewText(item.text);
  }, [refresh, item]);

  function readableItemState(state) {
    switch (state) {
      case "TODO":
        return <span className="todo-item">To-do</span>;
      case "IN_PROGRESS":
        return <span className="in-progress-item">In progress</span>;
      case "DONE":
        return <span className="done-item">Done</span>;
      default:
        break;
    }
  }

  return (
    <>
      <Dialog open={openItemDetailDialog} onClose={handleClose} fullWidth > 
        <BootstrapDialogTitle
          id="customized-dialog-title"
          onClose={handleClose}
        >
          <span className="item-detail-title">Item detail</span>
        </BootstrapDialogTitle>
        <DialogContent>
          <Typography variant="h3" className="item-detail-state">
            {readableItemState(item.state)} since{" "}
            <span style={{ color: "black" }}>{item.lastUpdateTime}</span>
          </Typography>
          <Card className="p-1 mtb-1" elevation={3} style={{height: "100%"}}>
            <Box>
              {editText ? (
                <ReactQuill
                style={{ marginTop: "1rem" }}
                value={newText}
                modules={fullScreenModules}
                formats={fullScreenFormats}
                onChange={setNewText}
              />
              ) : (
                <span className="item-text">{newText && parse(newText)}</span>
              )}
            </Box>
          </Card>
          <Typography variant="body1">
            {" "}
            <b>Created</b>:{item.creationTime}
          </Typography>
        </DialogContent>
        <DialogActions>
          {editText ? (
            <>
              <Button
                onClick={() => setEditText(false)}
                color="secondary"
                variant="contained"
              >
                {" "}
                Cancel{" "}
              </Button>
              <Button onClick={saveNewText} color="primary" variant="contained">
                Save
              </Button>
            </>
          ) : (
            <>
              <Button
                onClick={handleDeleteItem}
                color="secondary"
                variant="contained"
              >
                Delete
              </Button>
              <Button
                onClick={() => setEditText(true)}
                color="primary"
                variant="contained"
              >
                Edit
              </Button>
            </>
          )}
        </DialogActions>
      </Dialog>
      <Dialog open={openDelete} onClose={() => setOpenDelete(true)}>
        <DialogTitle>Are you sure you want to delete this item?</DialogTitle>
        <DialogActions>
          <Button
            onClick={() => setOpenDelete(false)}
            color="secondary"
            variant="contained"
          >
            Cancel
          </Button>
          <Button
            onClick={() => handleDelete(item.id)}
            color="primary"
            variant="contained"
          >
            Delete
          </Button>
        </DialogActions>
      </Dialog>
    </>
  );
}
