package main

import (
	// standard go libraries
	"io"
	"io/ioutil"
	"os"
	"strings"
	"path/filepath"
	"html/template"

	// open source go libraries
	"github.com/labstack/echo/v4"
)


// 환경에 따라서 root Path 바꿔줘야함
// 구름에서는 그냥 views
// 집컴에서는 C:/Users/ltw97/Desktop/Blog/blog/views

const (
	root	= "views"
	layout	= "layouts"
)

// Parsing with layout
func ParseTemplateWithLayout(rootDir string, layoutDir string, fileName string) (*template.Template, error) {
	cleanRoot := filepath.Clean(rootDir)
	cleanLayout := filepath.Clean(rootDir + "/" + layoutDir)
	pfx := len(cleanLayout) + 1
	root := template.New("")

	// Parsing layouts
	err := filepath.Walk(cleanLayout, func(path string, info os.FileInfo, e1 error) error{
		if !info.IsDir() && strings.HasSuffix(path, ".html") {
			if e1 != nil {
				return e1
			}
			
			b, e2 := ioutil.ReadFile(path)
			if e2 != nil {
				return e2
			}
			
			name := path[pfx:]
			t := root.New(name)
			t, e2 = t.Parse(string(b))
			if e2 != nil {
				return e2
			}
		}
		return nil
	})
	// -----------------------------------------
	
	// Parsing main template (ex: index.html...)
	filePfx := len(cleanRoot) + 1
	filePath := rootDir + "/" + fileName
	b, err := ioutil.ReadFile(filePath)
	if err != nil {
		panic(err)
	}
	
	name := filePath[filePfx:]
	t := root.New(name)
	t, err = t.Parse(string(b))
	if err != nil {
		panic(err)
	}
	// -------------------------------------------
	
	return root, err
}

// Template struct
type Template struct {
	templates *template.Template
}

func NewTemplate() *Template {
	newTemp := new(Template)

	return newTemp
}

func (t *Template) Render(w io.Writer, name string, data interface{}, c echo.Context) error {
	var err error 
	t.templates, err = ParseTemplateWithLayout(root, layout, name)
	if err != nil {
		return err
	}
	
	return t.templates.ExecuteTemplate(w, name, data)
}