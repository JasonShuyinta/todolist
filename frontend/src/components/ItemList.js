import React, { useState, useContext } from "react";
import { Paper, IconButton } from "@mui/material";
import ArrowLeftRoundedIcon from "@mui/icons-material/ArrowLeftRounded";
import ArrowRightRoundedIcon from "@mui/icons-material/ArrowRightRounded";
import axios from "axios";
import { SERVER_URL } from "./Constants";
import { Context } from "../context/Context";
import ItemDetail from "./ItemDetail";
import parse from "html-react-parser";

export default function ItemList({ items }) {
  const { user, refresh, setRefresh } = useContext(Context);

  const [openItemDetailDialog, setOpenItemDetailDialog] = useState(false);
  const [itemDetail, setItemDetail] = useState("");

  const openItemDetail = (item) => {
    setItemDetail(item);
    setOpenItemDetailDialog(true);
  };

  const handleClose = () => {
    setOpenItemDetailDialog(false);
  };

  const handleNextStep = (item) => {
    axios
      .post(`${SERVER_URL}/item/nextStep`, {
        id: item.id,
        text: item.text,
        state: item.state,
        user: user,
      })
      .then((res) => {
        if (res.status === 200) {
          setRefresh(!refresh);
        }
      })
      .catch((err) => alert("Something went wrong, please try again!"));
  };

  const handlePreviousStep = (item) => {
    axios
      .post(`${SERVER_URL}/item/previousStep`, {
        id: item.id,
        text: item.text,
        state: item.state,
        user: user,
      })
      .then((res) => {
        if (res.status === 200) {
          setRefresh(!refresh);
        }
      })
      .catch((err) => console.log(err));
  };

  return (
    <>
      {items &&
        items.length > 0 &&
        items.map((item) => {
          return (
            <div className="item-box" key={item.id}>
              <Paper
                elevation={3}
                className={`item-container ${
                  (item.state === "TODO" && "todo-container") ||
                  (item.state === "IN_PROGRESS" && "in-progress-container") ||
                  (item.state === "DONE" && "done-progress-container")
                }`}
              >
                {item.state === "IN_PROGRESS" || item.state === "DONE" ? (
                  <div className="center-and-start">
                    <IconButton onClick={() => handlePreviousStep(item)}>
                      <ArrowLeftRoundedIcon
                        fontSize="large"
                        className="next-btn"
                      />
                    </IconButton>
                  </div>
                ) : null}
                <div
                  className="center-and-start align-vertically w-80"
                  onClick={() => openItemDetail(item)}
                >
                  <span className="text-container">{parse(item.text)}</span>
                </div>
                {item.state === "TODO" || item.state === "IN_PROGRESS" ? (
                  <div className="center-and-end">
                    <IconButton onClick={() => handleNextStep(item)}>
                      <ArrowRightRoundedIcon
                        fontSize="large"
                        className="next-btn"
                      />
                    </IconButton>
                  </div>
                ) : null}
              </Paper>
            </div>
          );
        })}
      <ItemDetail
        item={itemDetail}
        openItemDetailDialog={openItemDetailDialog}
        handleClose={handleClose}
      />
    </>
  );
}
