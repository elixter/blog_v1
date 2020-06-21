ClassicEditor.create(document.querySelector('.form-content')).then(editor => {
  console.log(editor);
}).catch(err => {
  console.err(err);
});
