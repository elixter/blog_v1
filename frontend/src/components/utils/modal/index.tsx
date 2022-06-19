import CloseIcon from '../../../static/img/ico_close1.png';

type Props = {
	isOpen: boolean;
	setIsOpen: any;
	head: any;
	children: any;
};

const Modal = function (props: Props) {
	const { isOpen, setIsOpen, head, children } = props;

	const closeModal = () => {
		setIsOpen(false);
	};

	return (
		// eslint-disable-next-line react/jsx-no-useless-fragment
		<>
			{isOpen ? (
				<div className="dim-layer-auth">
					<div className="dim-bg" />
					<div id="layer" className="modal-layer">
						<button type="button" onClick={closeModal} className="modal-close js-modal-close">
							<img src={CloseIcon} alt="닫기" />
						</button>
						<div className="modal-page1">
							<div className="top">{head}</div>

							<div className="content">{children}</div>
						</div>
					</div>
				</div>
			) : null}
		</>
	);
};

export default Modal;
