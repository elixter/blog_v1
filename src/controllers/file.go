package controllers

import (
	"fmt"
	"github.com/labstack/echo/v4"
	"io"
	"log"
	"mime/multipart"
	"net/http"
	"os"
	"path/filepath"
	"time"
	
	"github.com/labstack/echo-contrib/session"
)

const (
	ImageSession   = "image"
)

// Reference : https://gist.github.com/tabula-rasa/0f1d55903e0cb0a3f221
// Thank you "tabula-rasa"!!

func CKUpload(c echo.Context) error {
	req := c.Request()
	if req.Method == "POST" {
		err := req.ParseMultipartForm(32 << 20)
		if err != nil {
			log.Println(err)
			return err
		}

		mpartFile, mpartHeader, err := req.FormFile("upload")
		if err != nil {
			log.Println(err)
			return err
		}
		defer mpartFile.Close()

		uri, err := fSave(mpartHeader, mpartFile)
		if err != nil {
			log.Println(err)
			return err
		}
		
		imgSess, err := session.Get(ImageSession, c)
		if err != nil {
			log.Println(err)
		}
		imgSess.Values[uri] = 0
		
		imgSess.Save(c.Request(), c.Response())

		CKEdFunc := req.FormValue("CKEditorFuncNum")
		fmt.Fprintln(c.Response().Writer, "<script>window.parent.CKEDITOR.tools.callFunction("+CKEdFunc+", \""+uri+"\");</script>")
	} else {
		err := fmt.Errorf("Method %q not allowed", req.Method)
		log.Printf("ERROR: %s\n", err)
		c.Error(err)
	}

	return c.String(http.StatusMethodNotAllowed, "shit")
}

func fSave(fh *multipart.FileHeader, f multipart.File) (string, error) {
	fileExt := filepath.Ext(fh.Filename)
	newName := fmt.Sprint(time.Now().Unix()) + fileExt //unique file name based on timestamp. You can keep original name and ignore duplicates
	uri := "/public/uploads/" + newName
	fullName := filepath.Join("public/uploads", newName)

	file, err := os.OpenFile(fullName, os.O_WRONLY|os.O_CREATE, 0666)
	if err != nil {
		return "", err
	}
	defer file.Close()
	_, err = io.Copy(file, f)
	if err != nil {
		return "", err
	}
	return uri, nil
}