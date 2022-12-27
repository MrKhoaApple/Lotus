package com.shop.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.entitty.Order;
import com.shop.entitty.OrderDetail;
import com.shop.repository.OrderDetailRepository;
import com.shop.repository.OrderRepository;
import com.shop.service.SendMailService;

@Service
public class SendMailUtil {

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderDetailRepository orderDetailRepository;

	@Autowired
	SendMailService sendMailService;

	public void sendMailOrder(Order order) {
		SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
		List<OrderDetail> listOrderDetails = orderDetailRepository.findByOrder(order);
		StringBuilder content = new StringBuilder();
		content.append(HEADER);
		for (OrderDetail oderDetail : listOrderDetails) {
			content.append("<tr>\r\n"
					+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 400; line-height: 24px; padding: 15px 10px 5px 10px;text-align: center;\">\r\n"
					+ "                                                        <img style=\"width: 85%;\" src="
					+ oderDetail.getProduct().getImage() + ">\r\n"
					+ "                                                    </td>\r\n"
					+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 400; line-height: 24px; padding: 15px 10px 5px 10px;\"> "
					+ oderDetail.getProduct().getName() + " </td>\r\n"
					+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 400; line-height: 24px; padding: 15px 10px 5px 10px;\"> "
					+ oderDetail.getQuantity() + " </td>\r\n"
					+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 400; line-height: 24px; padding: 15px 10px 5px 10px;\"> "
					+ format(String.valueOf(oderDetail.getPrice())) + " </td>\r\n"
					+ "                                                </tr>");
		}
		content.append(BODY2);
		content.append(
				"<td width=\"55%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 800; line-height: 24px; padding: 10px; border-top: 3px solid #eeeeee; border-bottom: 3px solid #eeeeee;\"> Total: </td>\r\n"
						+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 800; line-height: 24px; padding: 10px; border-top: 3px solid #eeeeee; border-bottom: 3px solid #eeeeee; color: red;\"> "
						+ format(String.valueOf(order.getAmount())) + " </td>");
		content.append(BODY3);
		content.append(
				"<td align=\"center\" valign=\"top\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 400; line-height: 24px;\">\r\n"
						+ "                                                            <p style=\"font-weight: 800;\">Delivery address</p>\r\n"
						+ "                                                            <p>" + order.getAddress()
						+ "</p>\r\n" + "                                                        </td>\r\n"
						+ "                                                    </tr>\r\n"
						+ "                                                </table>\r\n"
						+ "                                            </div>\r\n"
						+ "                                            <div style=\"display:inline-block; max-width:50%; min-width:240px; vertical-align:top; width:100%;\">\r\n"
						+ "                                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
						+ "                                                    <tr>\r\n"
						+ "                                                        <td align=\"center\" valign=\"top\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 400; line-height: 24px;\">\r\n"
						+ "                                                            <p style=\"font-weight: 800;\">Delivery date</p>\r\n"
						+ "                                                            <p>"
						+ dt.format(order.getOrderDate()) + "</p>\r\n"
						+ "                                                        </td>\r\n"
						+ "                                                    </tr>\r\n"
						+ "                                                </table>\r\n"
						+ "                                            </div>\r\n"
						+ "                                            <div style=\"display:inline-block; max-width:50%; min-width:240px; vertical-align:top; width:100%;\">\r\n"
						+ "                                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
						+ "                                                    <tr>\r\n"
						+ "                                                        <td align=\"center\" valign=\"top\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 400; line-height: 24px;\">\r\n"
						+ "                                                            <p style=\"font-weight: 800;\">Customer's name</p>\r\n"
						+ "                                                            <p>" + order.getUser().getName()
						+ "</p>\r\n" + "                                                        </td>\r\n"
						+ "                                                    </tr>\r\n"
						+ "                                                </table>\r\n"
						+ "                                            </div>\r\n"
						+ "                                            <div style=\"display:inline-block; max-width:50%; min-width:240px; vertical-align:top; width:100%;\">\r\n"
						+ "                                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
						+ "                                                    <tr>\r\n"
						+ "                                                        <td align=\"center\" valign=\"top\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 400; line-height: 24px;\">\r\n"
						+ "                                                            <p style=\"font-weight: 800;\">Phone number</p>\r\n"
						+ "                                                            <p>" + order.getPhone()
						+ "</p>\r\n" + "                                                        </td>");
		content.append(FOOTER);
		sendMailService.queue(order.getUser().getEmail(), "Delivered successfully", content.toString());
	}

	public void sendMailOrderSuccess(Order order) {
		SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
		List<OrderDetail> listOrderDetails = orderDetailRepository.findByOrder(order);
		StringBuilder content = new StringBuilder();
		content.append(HEADERSUCCESS);
		for (OrderDetail oderDetail : listOrderDetails) {
			content.append("<tr>\r\n"
					+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 400; line-height: 24px; padding: 15px 10px 5px 10px;text-align: center;\">\r\n"
					+ "                                                        <img style=\"width: 85%;\" src="
					+ oderDetail.getProduct().getImage() + ">\r\n"
					+ "                                                    </td>\r\n"
					+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 400; line-height: 24px; padding: 15px 10px 5px 10px;\"> "
					+ oderDetail.getProduct().getName() + " </td>\r\n"
					+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 400; line-height: 24px; padding: 15px 10px 5px 10px;\"> "
					+ oderDetail.getQuantity() + " </td>\r\n"
					+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 400; line-height: 24px; padding: 15px 10px 5px 10px;\"> "
					+ format(String.valueOf(oderDetail.getPrice())) + " </td>\r\n"
					+ "                                                </tr>");
		}
		content.append(BODY2);
		content.append(
				"<td width=\"55%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 800; line-height: 24px; padding: 10px; border-top: 3px solid #eeeeee; border-bottom: 3px solid #eeeeee;\"> Total: </td>\r\n"
						+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 800; line-height: 24px; padding: 10px; border-top: 3px solid #eeeeee; border-bottom: 3px solid #eeeeee; color: red;\"> "
						+ format(String.valueOf(order.getAmount())) + " </td>");
		content.append(BODY3);
		content.append(
				"<td align=\"center\" valign=\"top\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 400; line-height: 24px;\">\r\n"
						+ "                                                            <p style=\"font-weight: 800;\">Delivery address</p>\r\n"
						+ "                                                            <p>" + order.getAddress()
						+ "</p>\r\n" + "                                                        </td>\r\n"
						+ "                                                    </tr>\r\n"
						+ "                                                </table>\r\n"
						+ "                                            </div>\r\n"
						+ "                                            <div style=\"display:inline-block; max-width:50%; min-width:240px; vertical-align:top; width:100%;\">\r\n"
						+ "                                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
						+ "                                                    <tr>\r\n"
						+ "                                                        <td align=\"center\" valign=\"top\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 400; line-height: 24px;\">\r\n"
						+ "                                                            <p style=\"font-weight: 800;\">Delivery date</p>\r\n"
						+ "                                                            <p>"
						+ dt.format(order.getOrderDate()) + "</p>\r\n"
						+ "                                                        </td>\r\n"
						+ "                                                    </tr>\r\n"
						+ "                                                </table>\r\n"
						+ "                                            </div>\r\n"
						+ "                                            <div style=\"display:inline-block; max-width:50%; min-width:240px; vertical-align:top; width:100%;\">\r\n"
						+ "                                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
						+ "                                                    <tr>\r\n"
						+ "                                                        <td align=\"center\" valign=\"top\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 400; line-height: 24px;\">\r\n"
						+ "                                                            <p style=\"font-weight: 800;\">Customer's name</p>\r\n"
						+ "                                                            <p>" + order.getUser().getName()
						+ "</p>\r\n" + "                                                        </td>\r\n"
						+ "                                                    </tr>\r\n"
						+ "                                                </table>\r\n"
						+ "                                            </div>\r\n"
						+ "                                            <div style=\"display:inline-block; max-width:50%; min-width:240px; vertical-align:top; width:100%;\">\r\n"
						+ "                                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
						+ "                                                    <tr>\r\n"
						+ "                                                        <td align=\"center\" valign=\"top\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 400; line-height: 24px;\">\r\n"
						+ "                                                            <p style=\"font-weight: 800;\">Phone number</p>\r\n"
						+ "                                                            <p>" + order.getPhone()
						+ "</p>\r\n" + "                                                        </td>");
		content.append(FOOTER);
		sendMailService.queue(order.getUser().getEmail(), "Delivery success", content.toString());
	}
	public void sendMailOrderPaymented(Order order) {
		SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
		List<OrderDetail> listOrderDetails = orderDetailRepository.findByOrder(order);
		StringBuilder content = new StringBuilder();
		content.append(HEADERPAYMENT);
		for (OrderDetail oderDetail : listOrderDetails) {
			content.append("<tr>\r\n"
					+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 400; line-height: 24px; padding: 15px 10px 5px 10px;text-align: center;\">\r\n"
					+ "                                                        <img style=\"width: 85%;\" src="
					+ oderDetail.getProduct().getImage() + ">\r\n"
					+ "                                                    </td>\r\n"
					+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 400; line-height: 24px; padding: 15px 10px 5px 10px;\"> "
					+ oderDetail.getProduct().getName() + " </td>\r\n"
					+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 400; line-height: 24px; padding: 15px 10px 5px 10px;\"> "
					+ oderDetail.getQuantity() + " </td>\r\n"
					+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 400; line-height: 24px; padding: 15px 10px 5px 10px;\"> "
					+ format(String.valueOf(oderDetail.getPrice())) + " </td>\r\n"
					+ "                                                </tr>");
		}
		content.append(BODY2);
		content.append(
				"<td width=\"55%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 800; line-height: 24px; padding: 10px; border-top: 3px solid #eeeeee; border-bottom: 3px solid #eeeeee;\"> Total: </td>\r\n"
						+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 800; line-height: 24px; padding: 10px; border-top: 3px solid #eeeeee; border-bottom: 3px solid #eeeeee; color: red;\"> "
						+ format(String.valueOf(order.getAmount())) + " </td>");
		content.append(BODY3);
		content.append(
				"<td align=\"center\" valign=\"top\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 400; line-height: 24px;\">\r\n"
						+ "                                                            <p style=\"font-weight: 800;\">Delivery address</p>\r\n"
						+ "                                                            <p>" + order.getAddress()
						+ "</p>\r\n" + "                                                        </td>\r\n"
						+ "                                                    </tr>\r\n"
						+ "                                                </table>\r\n"
						+ "                                            </div>\r\n"
						+ "                                            <div style=\"display:inline-block; max-width:50%; min-width:240px; vertical-align:top; width:100%;\">\r\n"
						+ "                                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
						+ "                                                    <tr>\r\n"
						+ "                                                        <td align=\"center\" valign=\"top\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 400; line-height: 24px;\">\r\n"
						+ "                                                            <p style=\"font-weight: 800;\">Delivery date</p>\r\n"
						+ "                                                            <p>"
						+ dt.format(order.getOrderDate()) + "</p>\r\n"
						+ "                                                        </td>\r\n"
						+ "                                                    </tr>\r\n"
						+ "                                                </table>\r\n"
						+ "                                            </div>\r\n"
						+ "                                            <div style=\"display:inline-block; max-width:50%; min-width:240px; vertical-align:top; width:100%;\">\r\n"
						+ "                                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
						+ "                                                    <tr>\r\n"
						+ "                                                        <td align=\"center\" valign=\"top\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 400; line-height: 24px;\">\r\n"
						+ "                                                            <p style=\"font-weight: 800;\">Customer's name</p>\r\n"
						+ "                                                            <p>" + order.getUser().getName()
						+ "</p>\r\n" + "                                                        </td>\r\n"
						+ "                                                    </tr>\r\n"
						+ "                                                </table>\r\n"
						+ "                                            </div>\r\n"
						+ "                                            <div style=\"display:inline-block; max-width:50%; min-width:240px; vertical-align:top; width:100%;\">\r\n"
						+ "                                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
						+ "                                                    <tr>\r\n"
						+ "                                                        <td align=\"center\" valign=\"top\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 400; line-height: 24px;\">\r\n"
						+ "                                                            <p style=\"font-weight: 800;\">Phone number</p>\r\n"
						+ "                                                            <p>" + order.getPhone()
						+ "</p>\r\n" + "                                                        </td>");
		content.append(FOOTER);
		sendMailService.queue(order.getUser().getEmail(), "Paid successfully", content.toString());
	}

	public void sendMailOrderDeliver(Order order) {
		SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
		List<OrderDetail> listOrderDetails = orderDetailRepository.findByOrder(order);
		StringBuilder content = new StringBuilder();
		content.append(HEADERDELIVER);
		for (OrderDetail oderDetail : listOrderDetails) {
			content.append("<tr>\r\n"
					+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 400; line-height: 24px; padding: 15px 10px 5px 10px;text-align: center;\">\r\n"
					+ "                                                        <img style=\"width: 85%;\" src="
					+ oderDetail.getProduct().getImage() + ">\r\n"
					+ "                                                    </td>\r\n"
					+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 400; line-height: 24px; padding: 15px 10px 5px 10px;\"> "
					+ oderDetail.getProduct().getName() + " </td>\r\n"
					+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 400; line-height: 24px; padding: 15px 10px 5px 10px;\"> "
					+ oderDetail.getQuantity() + " </td>\r\n"
					+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 400; line-height: 24px; padding: 15px 10px 5px 10px;\"> "
					+ format(String.valueOf(oderDetail.getPrice())) + " </td>\r\n"
					+ "                                                </tr>");
		}
		content.append(BODY2);
		content.append(
				"<td width=\"55%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 800; line-height: 24px; padding: 10px; border-top: 3px solid #eeeeee; border-bottom: 3px solid #eeeeee;\"> Total: </td>\r\n"
						+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 800; line-height: 24px; padding: 10px; border-top: 3px solid #eeeeee; border-bottom: 3px solid #eeeeee; color: red;\"> "
						+ format(String.valueOf(order.getAmount())) + " </td>");
		content.append(BODY3);
		content.append(
				"<td align=\"center\" valign=\"top\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 400; line-height: 24px;\">\r\n"
						+ "                                                            <p style=\"font-weight: 800;\">Delivery address</p>\r\n"
						+ "                                                            <p>" + order.getAddress()
						+ "</p>\r\n" + "                                                        </td>\r\n"
						+ "                                                    </tr>\r\n"
						+ "                                                </table>\r\n"
						+ "                                            </div>\r\n"
						+ "                                            <div style=\"display:inline-block; max-width:50%; min-width:240px; vertical-align:top; width:100%;\">\r\n"
						+ "                                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
						+ "                                                    <tr>\r\n"
						+ "                                                        <td align=\"center\" valign=\"top\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 400; line-height: 24px;\">\r\n"
						+ "                                                            <p style=\"font-weight: 800;\">Delivery date</p>\r\n"
						+ "                                                            <p>"
						+ dt.format(order.getOrderDate()) + "</p>\r\n"
						+ "                                                        </td>\r\n"
						+ "                                                    </tr>\r\n"
						+ "                                                </table>\r\n"
						+ "                                            </div>\r\n"
						+ "                                            <div style=\"display:inline-block; max-width:50%; min-width:240px; vertical-align:top; width:100%;\">\r\n"
						+ "                                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
						+ "                                                    <tr>\r\n"
						+ "                                                        <td align=\"center\" valign=\"top\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 400; line-height: 24px;\">\r\n"
						+ "                                                            <p style=\"font-weight: 800;\">Customer's name</p>\r\n"
						+ "                                                            <p>" + order.getUser().getName()
						+ "</p>\r\n" + "                                                        </td>\r\n"
						+ "                                                    </tr>\r\n"
						+ "                                                </table>\r\n"
						+ "                                            </div>\r\n"
						+ "                                            <div style=\"display:inline-block; max-width:50%; min-width:240px; vertical-align:top; width:100%;\">\r\n"
						+ "                                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
						+ "                                                    <tr>\r\n"
						+ "                                                        <td align=\"center\" valign=\"top\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 400; line-height: 24px;\">\r\n"
						+ "                                                            <p style=\"font-weight: 800;\">Phone number</p>\r\n"
						+ "                                                            <p>" + order.getPhone()
						+ "</p>\r\n" + "                                                        </td>");
		content.append(FOOTER);
		sendMailService.queue(order.getUser().getEmail(), "Order has been confirmed", content.toString());
	}

	public void sendMailOrderCancel(Order order) {
		SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
		List<OrderDetail> listOrderDetails = orderDetailRepository.findByOrder(order);
		StringBuilder content = new StringBuilder();
		content.append(HEADERCANCEL);
		for (OrderDetail oderDetail : listOrderDetails) {
			content.append("<tr>\r\n"
					+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 400; line-height: 24px; padding: 15px 10px 5px 10px;text-align: center;\">\r\n"
					+ "                                                        <img style=\"width: 85%;\" src="
					+ oderDetail.getProduct().getImage() + ">\r\n"
					+ "                                                    </td>\r\n"
					+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 400; line-height: 24px; padding: 15px 10px 5px 10px;\"> "
					+ oderDetail.getProduct().getName() + " </td>\r\n"
					+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 400; line-height: 24px; padding: 15px 10px 5px 10px;\"> "
					+ oderDetail.getQuantity() + " </td>\r\n"
					+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 400; line-height: 24px; padding: 15px 10px 5px 10px;\"> "
					+ format(String.valueOf(oderDetail.getPrice())) + " </td>\r\n"
					+ "                                                </tr>");
		}
		content.append(BODY2);
		content.append(
				"<td width=\"55%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 800; line-height: 24px; padding: 10px; border-top: 3px solid #eeeeee; border-bottom: 3px solid #eeeeee;\"> Total: </td>\r\n"
						+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 800; line-height: 24px; padding: 10px; border-top: 3px solid #eeeeee; border-bottom: 3px solid #eeeeee; color: red;\"> "
						+ format(String.valueOf(order.getAmount())) + " </td>");
		content.append(BODY3);
		content.append(
				"<td align=\"center\" valign=\"top\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 400; line-height: 24px;\">\r\n"
						+ "                                                            <p style=\"font-weight: 800;\">Delivery address</p>\r\n"
						+ "                                                            <p>" + order.getAddress()
						+ "</p>\r\n" + "                                                        </td>\r\n"
						+ "                                                    </tr>\r\n"
						+ "                                                </table>\r\n"
						+ "                                            </div>\r\n"
						+ "                                            <div style=\"display:inline-block; max-width:50%; min-width:240px; vertical-align:top; width:100%;\">\r\n"
						+ "                                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
						+ "                                                    <tr>\r\n"
						+ "                                                        <td align=\"center\" valign=\"top\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 400; line-height: 24px;\">\r\n"
						+ "                                                            <p style=\"font-weight: 800;\">Delivery date</p>\r\n"
						+ "                                                            <p>"
						+ dt.format(order.getOrderDate()) + "</p>\r\n"
						+ "                                                        </td>\r\n"
						+ "                                                    </tr>\r\n"
						+ "                                                </table>\r\n"
						+ "                                            </div>\r\n"
						+ "                                            <div style=\"display:inline-block; max-width:50%; min-width:240px; vertical-align:top; width:100%;\">\r\n"
						+ "                                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
						+ "                                                    <tr>\r\n"
						+ "                                                        <td align=\"center\" valign=\"top\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 400; line-height: 24px;\">\r\n"
						+ "                                                            <p style=\"font-weight: 800;\">Customer's name</p>\r\n"
						+ "                                                            <p>" + order.getUser().getName()
						+ "</p>\r\n" + "                                                        </td>\r\n"
						+ "                                                    </tr>\r\n"
						+ "                                                </table>\r\n"
						+ "                                            </div>\r\n"
						+ "                                            <div style=\"display:inline-block; max-width:50%; min-width:240px; vertical-align:top; width:100%;\">\r\n"
						+ "                                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
						+ "                                                    <tr>\r\n"
						+ "                                                        <td align=\"center\" valign=\"top\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 400; line-height: 24px;\">\r\n"
						+ "                                                            <p style=\"font-weight: 800;\">Phone number</p>\r\n"
						+ "                                                            <p>" + order.getPhone()
						+ "</p>\r\n" + "                                                        </td>");
		content.append(FOOTER);
		sendMailService.queue(order.getUser().getEmail(), "Huỷ đơn successfully", content.toString());
	}

	public String format(String number) {
		DecimalFormat formatter = new DecimalFormat("###,###,###.##");

		return formatter.format(Double.valueOf(number)) + " VNĐ";
	}

	static String HEADERSUCCESS = "<body style=\"margin: 0 !important; padding: 0 !important; background-color: #eeeeee;\" bgcolor=\"#eeeeee\">\r\n"
			+ "    <div style=\"display: none; font-size: 1px; color: #fefefe; line-height: 1px; font-family: Open Sans, Helvetica, Arial, sans-serif; max-height: 0px; max-width: 0px; opacity: 0; overflow: hidden;\">\r\n"
			+ "        Lotus Gourmet \r\n"
			+ "    </div>\r\n" + "    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n"
			+ "        <tr>\r\n"
			+ "            <td align=\"center\" style=\"background-color: #eeeeee;\" bgcolor=\"#eeeeee\">\r\n"
			+ "                <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:600px;\">\r\n"
			+ "                    <tr>\r\n"
			+ "                        <td align=\"center\" valign=\"top\" style=\"font-size:0; padding: 35px;\" bgcolor=\"#005250\">\r\n"
			+ "                            <div style=\"display:inline-block; max-width:50%; min-width:100px; vertical-align:top; width:100%;\">\r\n"
			+ "                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
			+ "                                    <tr>\r\n"
			+ "                                        <td align=\"left\" valign=\"top\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 36px; font-weight: 800; line-height: 48px;\" class=\"mobile-center\">\r\n"
															//logo
//			+ "                                            <img src=\"https://asset.cloudinary.com/dnfyqflw5/6017d25c07f466f4d383ee9c3f16d5b6 width=\"220px\"/>\r\n"
			+ "                                        </td>\r\n" + "                                    </tr>\r\n"
			+ "                                </table>\r\n" + "                            </div>\r\n"
			+ "                            \r\n" + "                        </td>\r\n" + "                    </tr>\r\n"
			+ "                    <tr>\r\n"
			+ "                        <td align=\"center\" style=\"padding: 35px 35px 20px 35px; background-color: #ffffff;\" bgcolor=\"#ffffff\">\r\n"
			+ "                            <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:600px;\">\r\n"
			+ "                                <tr>\r\n"
			+ "                                    <td align=\"center\" style=\"font-family: Open sans-serif; font-size: 16px; font-weight: 400; line-height: 24px; padding-top: 25px;\">\r\n"
			+ "                                        <img src=\"https://res.cloudinary.com/veggie-shop/image/upload/v1634045009/assets/checked_pudgic.png?fbclid=IwAR2aTBpMU1Gbj8pVwuU6sH1lUAUEeK2U8df1mrI4zCyMT97OnjkEIbgBSQw\" width=\"115\" height=\"110\" style=\"display: block; border: 0px;\" /><br>\r\n"
			+ "                                        <h2 style=\"font-size: 30px; font-weight: 800; line-height: 36px; color: #333333; margin: 0;\"> Delivery success! </h2>\r\n"
			+ "                                        <p style=\"font-family: Open sans-serif; font-size: 18px;\"><em>Thanks for your order! Have a good day!</em></p>\r\n"
			+ "                                        <p style=\"font-family: Open sans-serif; font-size: 18px;\"><em>Nice to see you soon!</em></p>\r\n"
			+ "                                    </td>\r\n" + "                                </tr>\r\n"
			+ "                                \r\n" + "                                <tr>\r\n"
			+ "                                    <td align=\"left\" style=\"padding-top: 20px;\">\r\n"
			+ "                                        <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\">\r\n"
			+ "                                            <p style=\"font-size: 20px;font-family: Open sans-serif; text-decoration: underline; width: 200px;\">Order has been delivered:</p>\r\n"
			+ "                                            <tr>\r\n"
			+ "                                                <td width=\"25%\" align=\"left\" bgcolor=\"#eeeeee\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 800; line-height: 24px; padding: 10px;\">#</td>\r\n"
			+ "                                                <td width=\"25%\" align=\"left\" bgcolor=\"#eeeeee\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 800; line-height: 24px; padding: 10px;\">Name</td>\r\n"
			+ "                                                <td width=\"25%\" align=\"left\" bgcolor=\"#eeeeee\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 800; line-height: 24px; padding: 10px;\">Quantity</td>\r\n"
			+ "                                                <td width=\"25%\" align=\"left\" bgcolor=\"#eeeeee\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 800; line-height: 24px; padding: 10px;\">Total</td>\r\n"
			+ "                                            </tr>";

	static String HEADERDELIVER = "<body style=\"margin: 0 !important; padding: 0 !important; background-color: #eeeeee;\" bgcolor=\"#eeeeee\">\r\n"
			+ "    <div style=\"display: none; font-size: 1px; color: #fefefe; line-height: 1px; font-family: Open Sans, Helvetica, Arial, sans-serif; max-height: 0px; max-width: 0px; opacity: 0; overflow: hidden;\">\r\n"
			+ "        Lotus Gourmet\r\n"
			+ "    </div>\r\n" + "    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n"
			+ "        <tr>\r\n"
			+ "            <td align=\"center\" style=\"background-color: #eeeeee;\" bgcolor=\"#eeeeee\">\r\n"
			+ "                <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:600px;\">\r\n"
			+ "                    <tr>\r\n"
			+ "                        <td align=\"center\" valign=\"top\" style=\"font-size:0; padding: 35px;\" bgcolor=\"#005250\">\r\n"
			+ "                            <div style=\"display:inline-block; max-width:50%; min-width:100px; vertical-align:top; width:100%;\">\r\n"
			+ "                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
			+ "                                    <tr>\r\n"
			+ "                                        <td align=\"left\" valign=\"top\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 36px; font-weight: 800; line-height: 48px;\" class=\"mobile-center\">\r\n"
															//logo brand
//			+ "                                            <img src=\"https://asset.cloudinary.com/dnfyqflw5/6017d25c07f466f4d383ee9c3f16d5b6 width=\"220px\"/>\r\n"
			+ "                                        </td>\r\n" + "                                    </tr>\r\n"
			+ "                                </table>\r\n" + "                            </div>\r\n"
			+ "                            \r\n" + "                        </td>\r\n" + "                    </tr>\r\n"
			+ "                    <tr>\r\n"
			+ "                        <td align=\"center\" style=\"padding: 35px 35px 20px 35px; background-color: #ffffff;\" bgcolor=\"#ffffff\">\r\n"
			+ "                            <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:600px;\">\r\n"
			+ "                                <tr>\r\n"
			+ "                                    <td align=\"center\" style=\"font-family: Open sans-serif; font-size: 16px; font-weight: 400; line-height: 24px; padding-top: 25px;\">\r\n"
			+ "                                        <img src=\"https://res.cloudinary.com/veggie-shop/image/upload/v1634045009/assets/checked_pudgic.png?fbclid=IwAR2aTBpMU1Gbj8pVwuU6sH1lUAUEeK2U8df1mrI4zCyMT97OnjkEIbgBSQw\" width=\"115\" height=\"110\" style=\"display: block; border: 0px;\" /><br>\r\n"
			+ "                                        <h2 style=\"font-size: 30px; font-weight: 800; line-height: 36px; color: #333333; margin: 0;\"> Order has been confirmed! </h2>\r\n"
			+ "                                        <p style=\"font-family: Open sans-serif; font-size: 18px;\"><em>Thanks for your order! Your order will be delivered soon!</em></p>\r\n"
			+ "                                    </td>\r\n" + "                                </tr>\r\n"
			+ "                                \r\n" + "                                <tr>\r\n"
			+ "                                    <td align=\"left\" style=\"padding-top: 20px;\">\r\n"
			+ "                                        <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\">\r\n"
			+ "                                            <p style=\"font-size: 20px;font-family: Open sans-serif; text-decoration: underline; width: 200px;\">Order has been paid:</p>\r\n"
			+ "                                            <tr>\r\n"
			+ "                                                <td width=\"25%\" align=\"left\" bgcolor=\"#eeeeee\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 800; line-height: 24px; padding: 10px;\">#</td>\r\n"
			+ "                                                <td width=\"25%\" align=\"left\" bgcolor=\"#eeeeee\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 800; line-height: 24px; padding: 10px;\">Name</td>\r\n"
			+ "                                                <td width=\"25%\" align=\"left\" bgcolor=\"#eeeeee\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 800; line-height: 24px; padding: 10px;\">Quantity</td>\r\n"
			+ "                                                <td width=\"25%\" align=\"left\" bgcolor=\"#eeeeee\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 800; line-height: 24px; padding: 10px;\">Total</td>\r\n"
			+ "                                            </tr>";
	
	static String HEADERPAYMENT = "<body style=\"margin: 0 !important; padding: 0 !important; background-color: #eeeeee;\" bgcolor=\"#eeeeee\">\r\n"
			+ "    <div style=\"display: none; font-size: 1px; color: #fefefe; line-height: 1px; font-family: Open Sans, Helvetica, Arial, sans-serif; max-height: 0px; max-width: 0px; opacity: 0; overflow: hidden;\">\r\n"
			+ "        Lotus Gourmet\r\n"
			+ "    </div>\r\n" + "    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n"
			+ "        <tr>\r\n"
			+ "            <td align=\"center\" style=\"background-color: #eeeeee;\" bgcolor=\"#eeeeee\">\r\n"
			+ "                <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:600px;\">\r\n"
			+ "                    <tr>\r\n"
			+ "                        <td align=\"center\" valign=\"top\" style=\"font-size:0; padding: 35px;\" bgcolor=\"#005250\">\r\n"
			+ "                            <div style=\"display:inline-block; max-width:50%; min-width:100px; vertical-align:top; width:100%;\">\r\n"
			+ "                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
			+ "                                    <tr>\r\n"
			+ "                                        <td align=\"left\" valign=\"top\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 36px; font-weight: 800; line-height: 48px;\" class=\"mobile-center\">\r\n"
															//logo brand
			+ "                                            <img src=\"https://asset.cloudinary.com/dnfyqflw5/6017d25c07f466f4d383ee9c3f16d5b6\" width=\"220px\"/>\r\n"
			+ "                                        </td>\r\n" + "                                    </tr>\r\n"
			+ "                                </table>\r\n" + "                            </div>\r\n"
			+ "                            \r\n" + "                        </td>\r\n" + "                    </tr>\r\n"
			+ "                    <tr>\r\n"
			+ "                        <td align=\"center\" style=\"padding: 35px 35px 20px 35px; background-color: #ffffff;\" bgcolor=\"#ffffff\">\r\n"
			+ "                            <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:600px;\">\r\n"
			+ "                                <tr>\r\n"
			+ "                                    <td align=\"center\" style=\"font-family: Open sans-serif; font-size: 16px; font-weight: 400; line-height: 24px; padding-top: 25px;\">\r\n"
			+ "                                        <img src=\"https://res.cloudinary.com/veggie-shop/image/upload/v1634045009/assets/checked_pudgic.png?fbclid=IwAR2aTBpMU1Gbj8pVwuU6sH1lUAUEeK2U8df1mrI4zCyMT97OnjkEIbgBSQw\" width=\"115\" height=\"110\" style=\"display: block; border: 0px;\" /><br>\r\n"
			+ "                                        <h2 style=\"font-size: 30px; font-weight: 800; line-height: 36px; color: #333333; margin: 0;\"> Delivery success! </h2>\r\n"
			+ "                                        <p style=\"font-family: Open sans-serif; font-size: 18px;\"><em>Paid successfully! We will deliver to you soon!</em></p>\r\n"
			+ "                                        <p style=\"font-family: Open sans-serif; font-size: 18px;\"><em>Nice to see you soon!</em></p>\r\n"
			+ "                                    </td>\r\n" + "                                </tr>\r\n"
			+ "                                \r\n" + "                                <tr>\r\n"
			+ "                                    <td align=\"left\" style=\"padding-top: 20px;\">\r\n"
			+ "                                        <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\">\r\n"
			+ "                                            <p style=\"font-size: 20px;font-family: Open sans-serif; text-decoration: underline; width: 200px;\">Order has been delivered:</p>\r\n"
			+ "                                            <tr>\r\n"
			+ "                                                <td width=\"25%\" align=\"left\" bgcolor=\"#eeeeee\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 800; line-height: 24px; padding: 10px;\">#</td>\r\n"
			+ "                                                <td width=\"25%\" align=\"left\" bgcolor=\"#eeeeee\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 800; line-height: 24px; padding: 10px;\">Name</td>\r\n"
			+ "                                                <td width=\"25%\" align=\"left\" bgcolor=\"#eeeeee\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 800; line-height: 24px; padding: 10px;\">Quantity</td>\r\n"
			+ "                                                <td width=\"25%\" align=\"left\" bgcolor=\"#eeeeee\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 800; line-height: 24px; padding: 10px;\">Total</td>\r\n"
			+ "                                            </tr>";

	static String HEADERCANCEL = "<body style=\"margin: 0 !important; padding: 0 !important; background-color: #eeeeee;\" bgcolor=\"#eeeeee\">\r\n"
			+ "    <div style=\"display: none; font-size: 1px; color: #fefefe; line-height: 1px; font-family: Open Sans, Helvetica, Arial, sans-serif; max-height: 0px; max-width: 0px; opacity: 0; overflow: hidden;\">\r\n"
			+ "        Lotus Gourmet\r\n"
			+ "    </div>\r\n" + "    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n"
			+ "        <tr>\r\n"
			+ "            <td align=\"center\" style=\"background-color: #eeeeee;\" bgcolor=\"#eeeeee\">\r\n"
			+ "                <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:600px;\">\r\n"
			+ "                    <tr>\r\n"
			+ "                        <td align=\"center\" valign=\"top\" style=\"font-size:0; padding: 35px;\" bgcolor=\"#005250\">\r\n"
			+ "                            <div style=\"display:inline-block; max-width:50%; min-width:100px; vertical-align:top; width:100%;\">\r\n"
			+ "                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
			+ "                                    <tr>\r\n"
			+ "                                        <td align=\"left\" valign=\"top\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 36px; font-weight: 800; line-height: 48px;\" class=\"mobile-center\">\r\n"
															//logo brand
			+ "                                            <img src=\"https://res.cloudinary.com/dnfyqflw5/image/upload/v1670508874/LotusApp/Lotuslogo_lsrvob.png width=\"220px\"/>\r\n"
			+ "                                        </td>\r\n" + "                                    </tr>\r\n"
			+ "                                </table>\r\n" + "                            </div>\r\n"
			+ "                            \r\n" + "                        </td>\r\n" + "                    </tr>\r\n"
			+ "                    <tr>\r\n"
			+ "                        <td align=\"center\" style=\"padding: 35px 35px 20px 35px; background-color: #ffffff;\" bgcolor=\"#ffffff\">\r\n"
			+ "                            <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:600px;\">\r\n"
			+ "                                <tr>\r\n"
			+ "                                    <td align=\"center\" style=\"font-family: Open sans-serif; font-size: 16px; font-weight: 400; line-height: 24px; padding-top: 25px;\">\r\n"
			+ "                                        <img src=\"https://res.cloudinary.com/veggie-shop/image/upload/v1634046654/assets/cancellation_xhljqh.png\" width=\"115\" height=\"110\" style=\"display: block; border: 0px;\" /><br>\r\n"
			+ "                                        <h2 style=\"font-family: Open sans-serif;font-size: 30px; font-weight: 800; line-height: 36px; color: #333333; margin: 0;\"> Cancel order successfully! </h2>\r\n"
			+ "                                        <p style=\"font-family: Open sans-serif; font-size: 18px;\"><em>We very sorry about this problem, nice to see you soon!</em></p>\r\n"
			+ "                                    </td>\r\n" + "                                </tr>\r\n"
			+ "                                \r\n" + "                                <tr>\r\n"
			+ "                                    <td align=\"left\" style=\"padding-top: 20px;\">\r\n"
			+ "                                        <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\">\r\n"
			+ "                                            <p style=\"font-size: 20px;font-family: Open sans-serif; text-decoration: underline; width: 200px;\">Order has been cancelled:</p>\r\n"
			+ "                                            <tr>\r\n"
			+ "                                                <td width=\"25%\" align=\"left\" bgcolor=\"#eeeeee\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 800; line-height: 24px; padding: 10px;\">#</td>\r\n"
			+ "                                                <td width=\"25%\" align=\"left\" bgcolor=\"#eeeeee\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 800; line-height: 24px; padding: 10px;\">Name</td>\r\n"
			+ "                                                <td width=\"25%\" align=\"left\" bgcolor=\"#eeeeee\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 800; line-height: 24px; padding: 10px;\">Quantity</td>\r\n"
			+ "                                                <td width=\"25%\" align=\"left\" bgcolor=\"#eeeeee\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 800; line-height: 24px; padding: 10px;\">Total</td>\r\n"
			+ "                                            </tr>";

	static String HEADER = "<body style=\"margin: 0 !important; padding: 0 !important; background-color: #eeeeee;\" bgcolor=\"#eeeeee\">\r\n"
			+ "        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n"
			+ "            <tr>\r\n"
			+ "                <td align=\"center\" style=\"background-color: #eeeeee;\" bgcolor=\"#eeeeee\">\r\n"
			+ "                    <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:600px;\">\r\n"
			+ "                        <tr>\r\n"
			+ "                            <td align=\"center\" valign=\"top\" style=\"font-size:0; padding: 35px;\" bgcolor=\"#005250\">\r\n"
			+ "                                <div style=\"display:inline-block; max-width:50%; min-width:100px; vertical-align:top; width:100%;\">\r\n"
			+ "                                    <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
			+ "                                        <tr>\r\n"
			+ "                                            <td align=\"left\" valign=\"top\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 36px; font-weight: 800; line-height: 48px;\" class=\"mobile-center\">\r\n"
															//logo brand
			+ "                                            <img src=\"https://res.cloudinary.com/dnfyqflw5/image/upload/v1670500287/LotusApp/Artboard_1_gpnlcb.png\" width=\"220px\"/>\r\n"
			+ "                                            </td>\r\n"
			+ "                                        </tr>\r\n" + "                                    </table>\r\n"
			+ "                                </div>\r\n" + "\r\n" + "                            </td>\r\n"
			+ "                        </tr>\r\n" + "                        <tr>\r\n"
			+ "                            <td align=\"center\" style=\"padding: 35px 35px 20px 35px; background-color: #ffffff;\" bgcolor=\"#ffffff\">\r\n"
			+ "                                <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:600px;\">\r\n"
			+ "                                    <tr>\r\n"
			+ "                                        <td align=\"center\" style=\"font-family: Open sans-serif; font-size: 16px; font-weight: 400; line-height: 24px; padding-top: 25px;\">\r\n"
			+ "                                            <img src=\"https://res.cloudinary.com/veggie-shop/image/upload/v1634045009/assets/checked_pudgic.png?fbclid=IwAR2aTBpMU1Gbj8pVwuU6sH1lUAUEeK2U8df1mrI4zCyMT97OnjkEIbgBSQw\" width=\"115\" height=\"110\" style=\"display: block; border: 0px;\" /><br>\r\n"
			+ "                                            <h2 style=\"font-size: 30px; font-weight: 800; line-height: 36px; color: #333333; margin: 0;\"> Placed order successfully! </h2>\r\n"
			+ "                                            <p style=\"font-family: Open sans-serif; font-size: 18px;\"><em>Your order is processing , please wait patiently!</em></p>\r\n"
			+ "                                        </td>\r\n" + "                                    </tr>\r\n"
			+ "\r\n" + "                                    <tr>\r\n"
			+ "                                        <td align=\"left\" style=\"padding-top: 20px;\">\r\n"
			+ "                                            <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\">\r\n"
			+ "                                                <p style=\"font-size: 20px;font-family: Open sans-serif; text-decoration: underline; width: 200px;\">Your order:</p>\r\n"
			+ "                                                <tr>\r\n"
			+ "                                                    <td width=\"25%\" align=\"left\" bgcolor=\"#eeeeee\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 800; line-height: 24px; padding: 10px;\">#</td>\r\n"
			+ "                                                    <td width=\"25%\" align=\"left\" bgcolor=\"#eeeeee\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 800; line-height: 24px; padding: 10px;\">Name</td>\r\n"
			+ "                                                    <td width=\"25%\" align=\"left\" bgcolor=\"#eeeeee\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 800; line-height: 24px; padding: 10px;\">Quantity</td>\r\n"
			+ "                                                    <td width=\"25%\" align=\"left\" bgcolor=\"#eeeeee\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 800; line-height: 24px; padding: 10px;\">Total</td>\r\n"
			+ "                                                </tr>";
	static String BODY2 = "</table>\r\n" + "                                        </td>\r\n"
			+ "                                    </tr>\r\n" + "                                    <tr>\r\n"
			+ "                                        <td align=\"left\" style=\"padding-top: 20px;\">\r\n"
			+ "                                            <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\">\r\n"
			+ "                                                <tr>";
	static String BODY3 = "</tr>\r\n" + "                                            </table>\r\n"
			+ "                                        </td>\r\n" + "                                    </tr>\r\n"
			+ "                                </table>\r\n" + "                            </td>\r\n"
			+ "                        </tr>\r\n" + "                        <tr>\r\n"
			+ "                            <td align=\"center\" height=\"100%\" valign=\"top\" width=\"100%\" style=\"padding: 0 35px 35px 35px; background-color: #ffffff;\" bgcolor=\"#ffffff\">\r\n"
			+ "                                <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:660px;\">\r\n"
			+ "                                    <tr>\r\n"
			+ "                                        <td align=\"center\" valign=\"top\" style=\"font-size:0;\">\r\n"
			+ "                                            <div style=\"display:inline-block; max-width:50%; min-width:240px; vertical-align:top; width:100%;\">\r\n"
			+ "                                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
			+ "                                                    <tr>\r\n"
			+ "                                                        <td align=\"left\" valign=\"top\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 400; line-height: 24px;\">";
	static String BODY4 = "</td>\r\n" + "                                                    </tr>\r\n"
			+ "                                                </table>\r\n"
			+ "                                            </div>\r\n"
			+ "                                            <div style=\"display:inline-block; max-width:50%; min-width:240px; vertical-align:top; width:100%;\">\r\n"
			+ "                                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
			+ "                                                    <tr>\r\n"
			+ "                                                        <td align=\"left\" valign=\"top\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 400; line-height: 24px;\">";
	static String FOOTER = "</tr>\r\n" + "                                                </table>\r\n"
			+ "                                            </div>\r\n"
			+ "                                        </td>\r\n" + "                                    </tr>\r\n"
			+ "                                </table>\r\n" + "                            </td>\r\n"
			+ "                        </tr>\r\n" + "\r\n" + "                        <tr>\r\n"
			+ "                            <td align=\"center\" style=\"padding: 35px; background-color: #ffffff;\" bgcolor=\"#ffffff\">\r\n"
			+ "                                <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:600px;\">\r\n"
			+ "                                    <tr>\r\n"
			+ "                                        <td align=\"center\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 400; line-height: 24px; padding: 5px 0 10px 0;\">\r\n"
			+ "                                            <p style=\"font-size: 18px; font-weight: 800; line-height: 18px; color: #e0a141;\"> LotusGourmet </p>\r\n"
			+ "                                            <p style=\"font-family: Open sans-serif;\">Thanks for your order! Have a good day!</p>\r\n"
			+ "                                        </td>\r\n" + "                                    </tr>\r\n"
			+ "\r\n" + "                                </table>\r\n" + "                            </td>\r\n"
			+ "                        </tr>\r\n" + "                    </table>\r\n" + "                </td>\r\n"
			+ "            </tr>\r\n" + "        </table>\r\n" + "    </body>";
}
