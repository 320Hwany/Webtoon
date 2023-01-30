import React from "react";
import Welcome from "./Welcome";

function Hello(props){
    return(
        <div>
            <Welcome name="1 페이지" numOfPage={100}></Welcome>
            <Welcome name="2 페이지" numOfPage={200}></Welcome>
            <Welcome name="3 페이지" numOfPage={300}></Welcome>
        </div>
    );
}

export default Hello;